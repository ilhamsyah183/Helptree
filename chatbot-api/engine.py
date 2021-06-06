# imprt things that we need for the NLP
import json
import pickle
from tensorflow.python.framework import ops
import random
import tensorflow as tf
import tflearn
import numpy as np
import nltk
from Sastrawi.Stemmer.StemmerFactory import StemmerFactory
factory = StemmerFactory()
stemmer = factory.create_stemmer()

# import our chat-bot
with open('dataset_chatbot_helptree.json') as json_data:
    intents = json.load(json_data)

# clean up all of our data structures
data = pickle.load(open("training_data", "rb"))
words_data = data['words']
classes = data['classes']
x_train = data['train_x']
y_train = data['train_y']

# reset underlying graph data
ops.reset_default_graph()
# Build neural network
layer = tflearn.input_data(shape=[None, len(x_train[0])])
layer = tflearn.embedding(layer, input_dim=10000, output_dim=128)
layer = tflearn.lstm(layer, 256, dropout=0.8)
layer = tflearn.fully_connected(layer, len(y_train[0]), activation='softmax')
layer = tflearn.regression(layer, optimizer='adam', learning_rate=0.001, loss='categorical_crossentropy')

# Define model and setup tensorboard
model = tflearn.DNN(layer, tensorboard_dir='tflearn_logs')
# load our saved model
model.load('./model.tflearn')

def clean_up_sentence(sentence):
    # tokenize the pattern
    sentence_words = nltk.word_tokenize(sentence)
    # stem each word
    sentence_words = [stemmer.stem(word.lower()) for word in sentence_words]
    return sentence_words

# return bag of words array: 0 or 1 for each word
def bow(sentence, words_data, show_details=False):
    # tokenize the pattern
    sentence_words = clean_up_sentence(sentence)
    # bag of words
    bag = [0]*len(words_data)
    for s in sentence_words:
        for i, w in enumerate(words_data):
            if w == s:
                bag[i] = 1
                if show_details:
                    print("found in bag: %s" % w)
    return(np.array(bag))

ERROR_THRESHOLD = 0.25

def classify(sentence):
    if sentence is not None:
        # generate probabilities from the model
        results = model.predict([bow(sentence, words_data)])[0]
        # filter out predictions below a threshold
        results = [[i, r] for i, r in enumerate(results) if r > ERROR_THRESHOLD]
        # sort by strength of probability
        results.sort(key=lambda x: x[1], reverse=True)
        return_list = []
        for r in results:
            return_list.append((classes[r[0]], r[1]))
        # return tuple of intent and probability
        return return_list

def response(sentence, userID='helptree-user', show_details=True):
    if sentence is not None:
        result = classify(sentence)
        # if we have a classification then find the matching intent tag
        if results:
            # loop if there are matches to process
            while result:
                for i in intents['intents']:
                    # find a tag matching the first result
                    if i['tag'] == result[0][0]:
                        # a random response from the intent
                        bot = ''.join(random.choice(i['responses']))
                        return bot
                result.pop(0)
        else:
            bot = 'Maaf, chatbot tidak mengerti. Tanyakan hal lain kepadaku!'
            return bot