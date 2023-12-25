from flask import Flask, request, jsonify
from transformers import BertTokenizer, TFBertForSequenceClassification
import numpy as np
import static.crawler

app = Flask(__name__)

model = TFBertForSequenceClassification.from_pretrained("klue/bert-base", num_labels=3, from_pt=True)
tokenizer = BertTokenizer.from_pretrained('klue/bert-base')
max_seq_len = 128

@app.route('/analyze_sentiment', methods=['GET'])
def analyze_sentiment():
    coin_name = request.args.get('coin')
    if coin_name:
        data = static.crawler.getArticle(coin_name)
        res = '\n'.join(data)

        predicted_label = predict_sentiment(res)

        sentiment_classes = {0: 'Neutral', 1: 'Positive', 2: 'Negative'}
        sentiment = sentiment_classes[predicted_label]

        return jsonify({'coin': coin_name, 'sentiment': sentiment})
    else:
        return jsonify({'error': 'Please provide a valid coin name'})

def predict_sentiment(sentence):
    input_ids = tokenizer.encode(sentence, max_length=max_seq_len, pad_to_max_length=True)
    attention_mask = [1] * (max_seq_len - input_ids.count(tokenizer.pad_token_id)) + [0] * input_ids.count(tokenizer.pad_token_id)
    token_type_ids = [0] * max_seq_len

    input_ids = np.array([input_ids], dtype=int)
    attention_mask = np.array([attention_mask], dtype=int)
    token_type_ids = np.array([token_type_ids], dtype=int)

    prediction = model.predict([input_ids, attention_mask, token_type_ids])
    predicted_label = np.argmax(prediction[0][0], axis=-1)
    return predicted_label

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=False)
