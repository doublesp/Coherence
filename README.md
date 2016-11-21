# Garçon de cuisine

An app for dinner makers to search for recipe, create a shopping list, and send it to their significant others to have them buy the ingredients back.

# Flow

1. Home Activity
  * Portrait (Edit mode), is simply a ListView(RecyclerView) of items:
    1. Top section: ingredients on the shopping list. Swipe on existing ingredients to cross it out (i.e. mark as complete or undesirable), swipe again to un-cross it. Long press to remove it.
    2. First Row under top section: EditText that allows user put down specific ingredients. Tapping on enter will insert the ingredient to top section
    3. Last 10 row: Recipe suggestion based on user input. Tap on suggestions will atomatically insert the ingredients to the list.
  * Landscape (Browsing mode), a detail view that provides detail informations on the ingredients on the list.
    * fullscreen image background, show descriptions in the overlay to the right, and list all ingredients on the left for quick selection.
  
# Video Walkthrough

Here's a walkthrough of implemented user stories:

![Video Walkthrough](garcon.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).
