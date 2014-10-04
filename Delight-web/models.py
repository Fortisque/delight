from google.appengine.ext import db

class Receipt(db.Model):
    name = db.StringProperty(required=True)
    total_cost = db.FloatProperty()

class ReceiptsFoodItems(db.Model):
    food_item_key = db.StringProperty(required=True)
    receipt_key = db.StringProperty(required=True)

class Business(db.Model):
    name = db.StringProperty(required=True)

class User(db.Model):
    name = db.StringProperty(required=True)
    password = db.StringProperty(required=True)

class Review(db.Model):
    stars = db.IntegerProperty(required=True)
    comment = db.TextProperty()
    kind_of_review = db.StringProperty(required=True)
    created_at = db.DateTimeProperty(required=True)
    target = db.StringProperty()
    receipt_key = db.StringProperty(required=True)
    business_key = db.StringProperty(required=True)

class FoodItem(db.Model):
    name = db.StringProperty(required=True)
    cost = db.FloatProperty(required=True)
    picture = db.StringProperty()
    business_key = db.StringProperty()
