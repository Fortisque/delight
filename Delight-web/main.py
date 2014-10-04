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

from models import *

jinja_environment = jinja2.Environment(
    loader=jinja2.FileSystemLoader(os.path.dirname(__file__)))

query = Business.all(keys_only=True)
entries = query.fetch(1000)
db.delete(entries)

query = FoodItem.all(keys_only=True)
entries = query.fetch(1000)
db.delete(entries)

Business(name='Eureka').put()

a_business = Business.all().get()

FoodItem(name='test', cost=1.1, business_id=a_business.key().id())

class MainHandler(webapp2.RequestHandler):
    def get(self):
        template_values = {}
        template = jinja_environment.get_template("home.html")
        self.response.out.write(template.render(template_values))

class ReceiptHandler(webapp2.RequestHandler):
    def get(self):
        self.response.write("hello")

class ReviewHandler(webapp2.RequestHandler):
    def get(self):
        self.response.write("hello")

class AnalyzeHandler(webapp2.RequestHandler):
    def get(self):
        business_id = self.request.get("id")
        template_values = {}
        template_values['business_id'] = business_id
        template_values['food_items'] = FoodItem.all().filter('business_id =',business_id)
        template = jinja_environment.get_template("analyze.html")
        self.response.out.write(template.render(template_values))

app = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/receipt', ReceiptHandler),
    ('/review', ReviewHandler),
    ('/analyze', AnalyzeHandler)
], debug=True)
