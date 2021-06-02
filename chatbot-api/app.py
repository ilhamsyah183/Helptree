from helper import *
import numpy as np

#untuk API
from flask import Flask, render_template
from flask_restful import reqparse, Api, Resource

app = Flask(__name__)
api = Api(app)

# argument parsing
parser = reqparse.RequestParser()
parser.add_argument('query')


class PredictIntent(Resource):   
    def get(self):
        output = {}

        args = parser.parse_args()
        user_query = args['query']

        results = think(user_query, True)

        if results is not None:
            results = [[i,r] for i,r in enumerate(results) if r > ERROR_THRESHOLD ] 
            results.sort(key=lambda x: x[1], reverse=True) 
            return_results =[[classes[r[0]],r[1]] for r in results]
            intent_pred = return_results[0][0]
            confidence = return_results[0][1]

            response = ''

            if intent_pred == "rekomendasi_tanaman":
                response = "Kami menyarankan kamu untuk menanam tanaman yang mudah dipelihara seperti kaktus"
            if intent_pred == "selesai":
                response = "Senang bisa membantu, semangat ya!"
            if intent_pred == "salam_pembuka":
                response = "Hai, ada yang bisa kami bantu?"

            output = {'user_query': user_query, 'prediksi_intent': intent_pred, 'confidence': confidence, 'response': response}
            
            return output

# Setup the Api resource routing here
# Route the URL to the resource
api.add_resource(PredictIntent, '/bot')

@app.route('/')
def home():
    return render_template('home.html')

@app.route('/run')
def unduh_punkt():
    nltk.download('punkt')
    return 'Data NLTK punkt telah diunduh.'

if __name__ == '__main__':
    app.run(debug=True)