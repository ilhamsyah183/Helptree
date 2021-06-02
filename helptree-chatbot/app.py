from engine import *
import numpy as np

# untuk API
from flask import Flask, render_template
from flask_restful import reqparse, Api, Resource

app = Flask(__name__)
api = Api(app)

# argument parsing
parser = reqparse.RequestParser()
parser.add_argument('query')


class Predict(Resource):
    def get(self):
        output = {}
        args = parser.parse_args()
        user_query = args['query']

        bot_response = response(user_query)

        if bot_response is not None:
            output = {'user_query': user_query, 'bot_response': bot_response}

            return output


# Setup the Api resource routing here
# Route the URL to the resource
api.add_resource(Predict, '/chatbot')

@app.route('/')
def home():
    return render_template('home.html')

@app.route('/get-punkt')
def get_punkt():
	nltk.download('punkt')
	return 'punkt berhasil didownload'

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)