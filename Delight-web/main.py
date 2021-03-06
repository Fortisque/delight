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

BUSINESS_NAME = 'Eureka'

jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))

def gql_json_parser(query_obj):
    result = []
    for entry in query_obj:
        properties = dict([(p, unicode(getattr(entry, p))) for p in entry.properties()])
        properties['key'] = str(entry.key())

        del properties['business_key']
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
            food_item_keys.append(food_item_receipt.food_item_key)


        query_data = FoodItem.get(food_item_keys)
        if self.request.get('json'):
            json_data = {
                "website": "delight-food.appspot.com",
                "data": gql_json_parser(query_data),
                "receipt_key": receipt_key,
                "business_name": business.name,
                "business_key": str(business.key())
            }

            self.response.out.write(json.dumps(json_data))
        else:
            template_values['food_items'] = query_data
            template_values['QR_url'] = "http://api.qrserver.com/v1/create-qr-code/?data=%s&size=250x250" % (receipt_key,)
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
        #reviews = Review.gql("WHERE business_key = :1 AND kind_of_review = :2 AND created_at > :3 AND created_at < :4", str(business.key()), 'general', lower_limit, upper_limit)
        reviews = Review.gql("WHERE business_key = :1 AND kind_of_review = :2", str(business.key()), 'general')

        star_count = defaultdict(int)
        for review in reviews:
            star_count[floor(review.stars)] += 1


        template_values['reviews'] = reviews
        template_values['url'] = self.request.host
        template_values['star_count'] = star_count
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
                star_count[floor(review.stars)] += 1
                count += 1
                summation += review.stars

            if count == 0:
                count = 1
            average = summation * 1.0 / count
            food_items[food_item.name] = {
                "average": average,
                "picture": food_item.picture,
                "price": food_item.cost,
                "total": count,
                "key": str(food_item.key()),
                "star_count": star_count,
                "type": food_item.kind_of_food
            }

        template_values['food_items'] = json.dumps(food_items)

        template = jinja_environment.get_template("food.html")
        self.response.out.write(template.render(template_values))

class ReviewDishHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        template_values['name'] = self.request.get('name')

        today = datetime.today()
        yesterday = today - timedelta(1)
        lower_limit = self.request.get('lower_date') or yesterday
        upper_limit = self.request.get('upper_date') or today

        business = Business.all().filter('name =', BUSINESS_NAME).get()
        food_item = FoodItem.gql("WHERE name = :1 AND business_key = :2", template_values['name'], str(business.key())).get()
        #reviews = Review.gql("WHERE business_key = :1 AND target = :2 AND created_at > :3 AND created_at < :4", str(business.key()), str(food_item.key()), lower_limit, upper_limit)
        reviews = Review.gql("WHERE business_key = :1 AND target = :2", str(business.key()), str(food_item.key()))

        star_count = defaultdict(int)
        for review in reviews:
            star_count[floor(review.stars)] += 1

        template_values['picture'] = food_item.picture            
        template_values['reviews'] = reviews
        template_values['url'] = self.request.host
        template_values['star_count'] = star_count
        template = jinja_environment.get_template("dish.html")
        self.response.out.write(template.render(template_values))

class ReviewServerHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        today = datetime.today()
        yesterday = today - timedelta(1)
        lower_limit = self.request.get('lower_date') or yesterday
        upper_limit = self.request.get('upper_date') or today

        business = Business.all().filter('name =', BUSINESS_NAME).get()

        servers = {}
        #reviews = Review.gql("WHERE business_key = :1 AND kind_of_review = :2 AND created_at > :3 AND created_at < :4", str(business.key()), 'service', lower_limit, upper_limit)
        reviews = Review.gql("WHERE business_key = :1 AND kind_of_review = :2", str(business.key()), 'service')
        for review in reviews:
            server_name = review.target
            if server_name in servers:
                servers[server_name].append(review)
            else:
                servers[server_name] = [review]

        servers_data = {}
        for server_name in servers:
            star_count = defaultdict(int)
            summation = 0
            count = 0
            for review in servers[server_name]:
                star_count[floor(review.stars)] += 1
                count += 1
                summation += review.stars

            if count == 0:
                count = 1
            average = summation * 1.0 / count
            servers_data[server_name] = {
                "average": average,
                "total": count,
                "star_count": star_count
            }

        template_values['servers_data'] = json.dumps(servers_data)
        template = jinja_environment.get_template("server.html")
        self.response.out.write(template.render(template_values))

class ReviewIndividualHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}

        template_values['name'] = self.request.get('name')

        today = datetime.today()
        yesterday = today - timedelta(1)
        lower_limit = self.request.get('lower_date') or yesterday
        upper_limit = self.request.get('upper_date') or today

        business = Business.all().filter('name =', BUSINESS_NAME).get()
        #reviews = Review.gql("WHERE business_key = :1 AND target = :2 AND created_at > :3 AND created_at < :4", str(business.key()), template_values['name'], lower_limit, upper_limit)
        reviews = Review.gql("WHERE business_key = :1 AND target = :2", str(business.key()), template_values['name'])

        star_count = defaultdict(int)
        for review in reviews:
            star_count[floor(review.stars)] += 1

        template_values['reviews'] = reviews
        template_values['url'] = self.request.host
        template_values['star_count'] = star_count
        template = jinja_environment.get_template("dish.html")
        self.response.out.write(template.render(template_values))

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

        test_food_item = FoodItem(name='Fresno Fig Burger', cost=9.75, business_key=str(a_business), kind_of_food='Burger', picture='http://delight-food.appspot.com/static/fresno_fig_burger.png').put()
        test_food_item_2 = FoodItem(name='Fried Chicken Sliders', cost=12.75, business_key=str(a_business), kind_of_food='Signature', picture='http://www.foodrepublic.com/sites/default/files/imagecache/enlarge/recipe/sipsnbites_chickenslider.jpg').put()
        test_food_item_3 = FoodItem(name='Cowboy Burger', cost=7.75, business_key=str(a_business), kind_of_food='Burger', picture='http://delight-food.appspot.com/static/cowboy_burger.png').put()

        a_receipt = Receipt(name='test_receipt').put()

        ReceiptsFoodItems(food_item_key=str(test_food_item), receipt_key=str(a_receipt)).put()
        ReceiptsFoodItems(food_item_key=str(test_food_item_3), receipt_key=str(a_receipt)).put()

        receipt_key = str(a_receipt)
        data = [
            {
                'comment': 'meh',
                'target': '',
                'kind': 'general'
            },
            {
                'comment': 'sample data',
                'target': '',
                'kind': 'general'
            },
            {
                'comment': 'hi jennifer',
                'target': 'Jennifer',
                'kind': 'service'
            },
            {
                'comment': 'hi kevin',
                'target': 'Kevin',
                'kind': 'service'
            }
        ]
        for review in data:
            i = 0
            value = random.random() * 10 + 10
            while(i < value):
                review['stars'] = floor(random.random()*5 + 1)
                i += 1
                Review(stars=review['stars'], comment=review['comment'], created_at=datetime.now(), target=review['target'], receipt_key=receipt_key, kind_of_review=review['kind'], business_key=str(a_business)).put()

        data = [
            {
                'comment': 'great',
                'stars': 4.0,
                'target': str(test_food_item),
                'kind': 'food'
            },
            {
                'comment': 'terrible',
                'stars': 2.0,
                'target': str(test_food_item_2),
                'kind': 'food'
            },
            {
                'comment': 'edible',
                'stars': 3.0,
                'target': str(test_food_item_3),
                'kind': 'food'
            },
        ]

        for review in data:
            Review(stars=review['stars'], comment=review['comment'], created_at=datetime.now(), target=review['target'], receipt_key=receipt_key, kind_of_review=review['kind'], business_key=str(a_business)).put()

        self.response.write("success")

class BatchReviewHandler(webapp2.RequestHandler):
    def post(self):
        all_data = json.loads(self.request.body)
        data = json.loads(all_data['data'])
        receipt_key = all_data['receipt_key']
        business_key = all_data['business_key']
        receipt = Receipt.get(receipt_key)

        for review in data:
            if 'comment' not in review:
                review['comment'] = ''
            Review(stars=float(review['stars']), comment=review['comment'], created_at=datetime.now(), target=review['target'], receipt_key=receipt_key, kind_of_review=review['kind'], business_key=business_key).put()

        self.response.headers['Content-Type'] = 'application/json'
        obj = {
            'success': 'yes', 
        } 
        self.response.write(json.dumps(obj))

class ResponseHandler(webapp2.RequestHandler):
    def get(self):
        business = Business.all().filter('name =', BUSINESS_NAME).get()

        review = Review.get(self.request.get('key'))

        template_values = {}

        template_values['review'] = review

        template = jinja_environment.get_template("response.html")
        self.response.out.write(template.render(template_values))


app = webapp2.WSGIApplication([
    ('/', ReviewGeneralHandler),
    ('/receipt', ReceiptHandler),
    ('/review/food', ReviewFoodHandler),
    ('/review/food/dish', ReviewDishHandler),
    ('/review/server', ReviewServerHandler),
    ('/review/server/individual', ReviewIndividualHandler),
    ('/review/', ReviewGeneralHandler),
    ('/batch_reviews', BatchReviewHandler),
    ('/reset_and_seed', ResetAndSeedHandler),
    ('/response', ResponseHandler)
], debug=True)
