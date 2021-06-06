from engine import *
import numpy as np

# untuk API
from flask import Flask, render_template
from flask_restful import reqparse, Api, Resource

app = Flask(__name__)
api = Api(app)

#parsing argument
parse = reqparse.RequestParser()
parse.add_argument('query')

class Predict(Resource):
    def get(self):
        output = {}
        args = parse.parse_args()
        user_query = args['query']
        responsebot = response(user_query)
        if responsebot is not None:
            output = {'user_query': user_query, 'bot_response': responsebot}

            return output

# Setup the Api resource routing here
# Route  URL to the resource
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