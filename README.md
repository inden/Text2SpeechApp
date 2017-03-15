# (Demo) Text2SpeechApp #
A demo app to test TextToSpeech module. It reads cooking recepies.
The reading quality is quite good.

Recepies are stored in xml format. 
It is parsed by XmlPullParser and transformed in HTML string for the display. 
When the TextToSpeech reads the text, I add some silence between phrases to make it easier to be understood. 
The length of silence varies according to Html tag types.

## How to use ##
Click a recepi on the list of cooking recipies

<img src="https://cloud.githubusercontent.com/assets/21304543/23956317/a5fd492a-099c-11e7-8797-53a4517922c2.png" width="400"/>

It shows the recepi. Click the "speak" image button to listen.

<img src="https://cloud.githubusercontent.com/assets/21304543/23956316/a5e564a4-099c-11e7-8390-1378eeea750c.png" width="400"/>

Click the "stop" image button to stop.

<img src="https://cloud.githubusercontent.com/assets/21304543/23956318/a62054c4-099c-11e7-833c-75c43d0048ef.png" width="400"/>


## API 19 or later required ##

## Technology ##
- TextToSpeech from Google
- XmlPullParser
- WebView
- RecyclerView, CardView
