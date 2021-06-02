import nltk
import json
import numpy as np
from Sastrawi.Stemmer.StemmerFactory import StemmerFactory

# probability threshold
ERROR_THRESHOLD = 0.2
# load our calculated synapse values
synapse_file = 'synapses.json' 
with open(synapse_file) as data_file: 
    synapse = json.load(data_file) 
    synapse_0 = np.asarray(synapse['synapse0']) 
    synapse_1 = np.asarray(synapse['synapse1'])
    words = synapse['words']
    classes = synapse['classes']

factory = StemmerFactory()
stemmer = factory.create_stemmer()

# compute sigmoid nonlinearity
def sigmoid(x):
    output = 1/(1+np.exp(-x))
    return output

def clean_up_sentence(sentence):
    # tokenize the pattern
    sentence_words = nltk.word_tokenize(sentence)
    # stem each word
    sentence_words = [stemmer.stem(word.lower()) for word in sentence_words]
    return sentence_words

# return bag of words array: 0 or 1 for each word in the bag that exists in the sentence
def bow(sentence, words, show_details=True):
    # tokenize the pattern
    sentence_words = clean_up_sentence(sentence)
    word_in_bag = []
    # bag of words
    bag = [0]*len(words)  
    for s in sentence_words:
        for i,w in enumerate(words):
            if w == s: 
                bag[i] = 1
                if show_details:
                    word_in_bag.append(w)
    #check if words found in bag
    if show_details:
        if not word_in_bag:
            print("Tidak menemukan kata yang sama didalam \"bag\" ")
        else:
            print ("Kata dalam kalimat input yang ditemukan didalam \"bag\": %s" % word_in_bag)
    return(np.array(bag))

def think(sentence, show_details=True):
    if sentence is not None:
        bow_num = bow(sentence.lower(), words)
        if show_details:
            print ("bow:", bow_num)
        # input layer is our bag of words
        l0 = bow_num
        # matrix multiplication of input and hidden layer
        l1 = sigmoid(np.dot(l0, synapse_0))
        # output layer
        l2 = sigmoid(np.dot(l1, synapse_1))
        return l2