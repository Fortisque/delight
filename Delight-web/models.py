from google.appengine.ext import db

class FoodItemsReceipts(db.Model):
    receipt_id = db.IntegerProperty(required=True)
    food_id = db.IntegerProperty(required=True)

class Receipt(db.Model):
    name = db.StringProperty(required=True)
    picture = db.StringProperty(required=True)
    cost = db.StringProperty()
    receipt_id = db.IntegerProperty(required=True)

class Business(db.Model):
    name = db.StringProperty(required=True)

class User(db.Model):
    name = db.StringProperty(required=True)
    password = db.StringProperty(required=True)
    cost = db.StringProperty(required=True)

class Review(db.Model):
    stars = db.IntegerProperty(required=True)
    comment = db.TextProperty(required=True)
    food_item_id = db.IntegerProperty()

class FoodItem(db.Model):
    name = db.StringProperty(required=True)
    cost = db.FloatProperty(required=True)
    picture = db.StringProperty()
    business_name = db.StringProperty()
