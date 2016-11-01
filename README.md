# listgen

An app that lets you list ideas you've had in mind, e.g. shopping list, travel plan, foods/movies/books to recommend ... e.t.c. With the help of contexual idea recommendation, creating the list should be as simple as tap, tap, tap rather than type, type, type.

# Flow

1. Selection Activity
  * Choose sub-category -> go to 2.
  * Suprise Me (Random list generator) -> go to 2
  * History - Select previously created list -> go to 2
2. List Activity
  * Toolbar Items: Save, Share, Camera (nice to have, import a list from picture of existing list)
  * Portrait (Edit mode), is simply a ListView(RecyclerView) of items:
    1. Row 1: location based suggestsion per category (e.g. Nearby restaurants, Point of Interests, ... e.t.c)
    2. Row 2: EditText that allows user put down ideas. Tapping on enter will insert a new row under this row. Swipe on existing ideas to cross it out (i.e. mark as complete or undesirable), swipe again to un-cross it. Long press to remove it.
    3. Last 10 row: Idea suggestions based on user input.
  * Landscape (Browsing mode), a detail view that provides detail informations on the ideas you've put down.
  
# Categories

* Entertainment
  * Movie
  * Drama
  * Music
  * Concerts
  * Books
* Shopping
  * Groceries
  * Clothings
  * Electronics
  * Mobiles
* Locations
  * Hikes
  * Points of Interest
* Food
  * Restaurants
  * Recipes
