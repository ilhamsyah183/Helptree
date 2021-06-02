# things we need for NLP
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

# import our chat-bot intents file
with open('dataset_chatbot_helptree.json') as json_data:
    intents = json.load(json_data)

# restore all of our data structures
data = pickle.load(open("training_data", "rb"))
words = data['words']
classes = data['classes']
train_x = data['train_x']
train_y = data['train_y']

# reset underlying graph data
ops.reset_default_graph()
# Build neural network
net = tflearn.input_data(shape=[None, len(train_x[0])])
net = tflearn.embedding(net, input_dim=10000, output_dim=128)
net = tflearn.lstm(net, 256, dropout=0.8)
net = tflearn.fully_connected(net, len(train_y[0]), activation='softmax')
net = tflearn.regression(net, optimizer='adam', learning_rate=0.001, loss='categorical_crossentropy')

# Define model and setup tensorboard
model = tflearn.DNN(net, tensorboard_dir='tflearn_logs')
# load our saved model
model.load('./model.tflearn')


def clean_up_sentence(sentence):
    # tokenize the pattern
    sentence_words = nltk.word_tokenize(sentence)
    # stem each word
    sentence_words = [stemmer.stem(word.lower()) for word in sentence_words]
    return sentence_words

# return bag of words array: 0 or 1 for each word in the bag that exists in the sentence


def bow(sentence, words, show_details=False):
    # tokenize the pattern
    sentence_words = clean_up_sentence(sentence)
    # bag of words
    bag = [0]*len(words)
    for s in sentence_words:
        for i, w in enumerate(words):
            if w == s:
                bag[i] = 1
                if show_details:
                    print("found in bag: %s" % w)
    return(np.array(bag))


ERROR_THRESHOLD = 0.25


def classify(sentence):
    if sentence is not None:
        # generate probabilities from the model
        results = model.predict([bow(sentence, words)])[0]
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
        results = classify(sentence)
        # if we have a classification then find the matching intent tag
        if results:
            # loop as long as there are matches to process
            while results:
                for i in intents['intents']:
                    # find a tag matching the first result
                    if i['tag'] == results[0][0]:
                        # a random response from the intent
                        bot = ''.join(random.choice(i['responses']))
                        return bot
                results.pop(0)
        else:
            bot = 'Maaf, chatbot tidak mengerti. Tanyakan hal lain kepadaku!'
            return bot