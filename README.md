# GooglePlace

<H3>How to Use</H3>

```java
//From Activity
LocationAutocompleteJson locationJson = new LocationAutocompleteJson(MainActivity.this, AppConstant.GOOGLE_PLACE_API_KEY, <Search Term>);

//From Fragment
LocationAutocompleteJson locationJson = new LocationAutocompleteJson(getActivity(), MyFragment.this, AppConstant.GOOGLE_PLACE_API_KEY, <Search Term>);
```

<H4>Implement Interface LocationAutocompleteInterface with Activity Or Fragment</H4>
```java
@Override
    public void getLocationAutocompleteDataList(String status, ArrayList<PredictionModel> predictionList) {
        if (status.equals("OK")) {
            ArrayList<String> placeList = new ArrayList<>();
            for (int i = 0; i < predictionList.size(); i++) {
                placeList.add(predictionList.get(i).getDescription());
            }
           //Apply code with placeList...
        }
    }
 ```
 
<H3>For auto Complate Place</H3>
<H4>Define</H4>
```java
AutoCompleteTextView acPlace;

LocationAutocompleteJson locationJson;  
ArrayList<String> placeList;  
ArrayAdapter<String> placeListAdapter;
```

<H4>In onCreate() Method</H4>
```java
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        acPlace = (AutoCompleteTextView) view.findViewById(R.id.acPlace);
        acPlace.addTextChangedListener(placeAutoCompleteWatcher);
  }
  
private TextWatcher placeAutoCompleteWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            locationJson = new LocationAutocompleteJson(MainActivity.this, AppConstant.GOOGLE_PLACE_API_KEY, charSequence.toString());
            locationJson.execute();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
    
    @Override
    public void getLocationAutocompleteDataList(String status, ArrayList<PredictionModel> predictionList) {
        if (status.equals("OK")) {
            placeList = new ArrayList<>();
            for (int i = 0; i < predictionList.size(); i++) {
                placeList.add(predictionList.get(i).getDescription());
            }
            placeListAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, placeList);
            acPlace.setAdapter(placeListAdapter);
        }
    }

```
