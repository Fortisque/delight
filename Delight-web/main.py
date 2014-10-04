#!/usr/bin/env python
#
# Copyright 2007 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
import webapp2
import jinja2
import os
import logging
import json
import urllib2
from datetime import datetime, date, timedelta
from collections import defaultdict
import random
from math import floor

from models import *

BUSINESS_NAME = 'Gecko Gecko'

jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))

def gql_json_parser(query_obj):
    result = []
    for entry in query_obj:
        properties = dict([(p, unicode(getattr(entry, p))) for p in entry.properties()])
        properties['target'] = str(entry.key())
        result.append(properties)

    return result

class MainHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}
        template = jinja_environment.get_template("home.html")
        self.response.out.write(template.render(template_values))

class ReceiptHandler(webapp2.RequestHandler):
    def get(self):

        business = Business.all().filter('name =', BUSINESS_NAME).get()

        receipt_key = self.request.get('id')

        receipt = Receipt.all().get()
        receipt_key = str(receipt.key())

        template_values = {}

        food_item_keys = []
        template_values['food_items'] = []
        for food_item_receipt in ReceiptsFoodItems.gql("WHERE receipt_key = :1", receipt_key):
            food_item_keys.append(food_item_receipt.target)

        query_data = FoodItem.get(food_item_keys)

        json_data = {
            'website': 'delight-food.appspot.com',
            'data': json.dumps(gql_json_parser(query_data)),
            'receipt_key': receipt_key,
            'business_name': business.name,
            'business_key': str(business.key())
        }

        template_values['food_items'] = query_data
        template_values['json_query_data'] = json_data
        template_values['QR_url'] = 'http://api.qrserver.com/v1/create-qr-code/?data=%s&size=100x100' % (template_values['json_query_data'],)
        template = jinja_environment.get_template("receipt.html")
        self.response.out.write(template.render(template_values))

    def post(self):
        self.response.write("hello")

class ReviewGeneralHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}
        today = datetime.today()
        yesterday = today - timedelta(1)
        lower_limit = self.request.get('lower_date') or yesterday
        upper_limit = self.request.get('upper_date') or today

        business = Business.all().filter('name =', BUSINESS_NAME).get()
        reviews = Review.gql("WHERE business_key = :1 AND kind_of_review = :2 AND created_at > :3 AND created_at < :4", str(business.key()), 'general', lower_limit, upper_limit)

        star_count = defaultdict(int)
        for review in reviews:
            star_count[review.stars] += 1


        template_values['reviews'] = reviews
        template_values['star_count'] = json.dumps(star_count)
        template = jinja_environment.get_template("general.html")
        self.response.out.write(template.render(template_values))

class ReviewFoodHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        today = datetime.today()
        yesterday = today - timedelta(1)
        lower_limit = self.request.get('lower_date') or yesterday
        upper_limit = self.request.get('upper_date') or today

        business = Business.all().filter('name =', BUSINESS_NAME).get()

        food_items = {}
        for food_item in FoodItem.gql("WHERE business_key = :1", str(business.key())):
            reviews = Review.gql("WHERE target = :1 AND kind_of_review = :2 AND created_at > :3 AND created_at < :4", str(food_item.key()), 'food', lower_limit, upper_limit)
            star_count = defaultdict(int)
            summation = 0
            count = 0
            for review in reviews:
                star_count[review.stars] += 1
                count += 1
                summation += review.stars

            if count == 0:
                count = 1
            average = summation * 1.0 / count
            food_items[food_item.name] = {
                "average": average,
                "key": str(food_item.key()),
                "star_count": star_count
            }

        template_values['food_items'] = json.dumps(food_items)

        template = jinja_environment.get_template("food.html")
        self.response.out.write(template.render(template_values))

class ReviewServerHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        today = datetime.today()
        yesterday = today - timedelta(1)
        lower_limit = self.request.get('lower_date') or yesterday
        upper_limit = self.request.get('upper_date') or today

        business = Business.all().filter('name =', BUSINESS_NAME).get()

        food_items = {}
        for food_item in FoodItem.gql("WHERE business_key = :1", str(business.key())):
            reviews = Review.gql("WHERE target = :1 AND kind_of_review = :2 AND created_at > :3 AND created_at < :4", str(food_item.key()), 'food', lower_limit, upper_limit)
            star_count = defaultdict(int)
            summation = 0
            count = 0
            for review in reviews:
                star_count[review.stars] += 1
                count += 1
                summation += review.stars

            if count == 0:
                count = 1
            average = summation * 1.0 / count
            food_items[food_item.name] = {
                "average": average,
                "key": str(food_item.key()),
                "star_count": star_count
            }

        template_values['food_items'] = json.dumps(food_items)

        template = jinja_environment.get_template("server.html")
        self.response.out.write(template.render(template_values))

class AnalyzeHandler(webapp2.RequestHandler):
    def get(self):
        business = Business.all().filter('name =', BUSINESS_NAME).get()
        template_values = {}
        template_values['food_items'] = FoodItem.gql("WHERE business_key = :1", str(business.key()))
        template = jinja_environment.get_template("analyze.html")
        self.response.out.write(template.render(template_values))

class FoodHandler(webapp2.RequestHandler):
    def get(self):
        self.response.write("hello")

class ResetAndSeedHandler(webapp2.RequestHandler):
    def get(self):
        query = Business.all(keys_only=True)
        entries = query.fetch(1000)
        db.delete(entries)

        query = FoodItem.all(keys_only=True)
        entries = query.fetch(1000)
        db.delete(entries)

        query = ReceiptsFoodItems.all(keys_only=True)
        entries = query.fetch(1000)
        db.delete(entries)

        query = Receipt.all(keys_only=True)
        entries = query.fetch(1000)
        db.delete(entries)

        query = Review.all(keys_only=True)
        entries = query.fetch(1000)
        db.delete(entries)

        a_business = Business(name=BUSINESS_NAME).put()

        test_food_item = FoodItem(name='test', cost=1.1, business_key=str(a_business)).put()
        test_food_item_2 = FoodItem(name='test2', cost=1.2, business_key=str(a_business)).put()

        a_receipt = Receipt(name='test_receipt').put()

        ReceiptsFoodItems(food_item_key=str(test_food_item), receipt_key=str(a_receipt)).put()
        ReceiptsFoodItems(food_item_key=str(test_food_item_2), receipt_key=str(a_receipt)).put()

        receipt_key = str(a_receipt)
        data = [
            {
                'comment': 'great',
                'target': str(test_food_item),
                'kind': 'food'
            },
            {
                'comment': 'nice',
                'target': str(test_food_item),
                'kind': 'food'
            },
            {
                'comment': 'terrible',
                'target': str(test_food_item_2),
                'kind': 'food'
            },
            {
                'comment': 'edible',
                'target': str(test_food_item),
                'kind': 'food'
            },
            {
                'comment': 'nice place',
                'target': '',
                'kind': 'general'
            },
            {
                'comment': 'love it',
                'target': '',
                'kind': 'general'
            },
            {
                'comment': 'hi bob',
                'target': '',
                'kind': 'service'
            },
            {
                'comment': 'hi jim',
                'target': '',
                'kind': 'service'
            }
        ]
        for review in data:
            i = 0
            value = random.random() * 10 + 10
            while(i < value):
                review['stars'] = int(floor(random.random()*5 + 1))
                i += 1
                Review(stars=review['stars'], comment=review['comment'], created_at=datetime.now(), target=review['target'], receipt_key=receipt_key, kind_of_review=review['kind'], business_key=str(a_business)).put()

        self.response.write("success")

class BatchReviewHandler(webapp2.RequestHandler):
    def get(self):
        data = self.request.get('data')
        receipt_key = self.request.get('receipt_key')
        business_key = self.request.get('business_key')
        receipt = Receipt.get(receipt_key)

        for review in data:
            Receipt(stars=review['stars'], comment=review['comment'], created_at=datetime.now(), target=review['target'], receipt_key=receipt_key, kind_of_review=review['kind'], business_key=business_key).put()

        self.response.headers['Content-Type'] = 'application/json'
        self.response.write("success")

app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/receipt', ReceiptHandler),
    ('/review/food', ReviewFoodHandler),
    ('/review/server', ReviewServerHandler),
    ('/review/*', ReviewGeneralHandler),
    ('/batch_reviews', BatchReviewHandler),
    ('/reset_and_seed', ResetAndSeedHandler)
], debug=True)
