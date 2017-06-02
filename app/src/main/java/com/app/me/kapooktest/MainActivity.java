package com.app.me.kapooktest;

import static com.app.me.kapooktest.helper.FacebookHelper.*;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.app.me.kapooktest.customclass.AllContentAdapter;
import com.app.me.kapooktest.customclass.CustomViewPager;
import com.app.me.kapooktest.customclass.EndlessRecyclerViewScrollListener;
import com.app.me.kapooktest.customclass.HotContentAdapter;
import com.app.me.kapooktest.customclass.JsonHelper;
import com.app.me.kapooktest.customclass.NewContentAdapter;
import com.app.me.kapooktest.customclass.VertialRecAdapter;
import com.app.me.kapooktest.helper.FacebookHelper;
import com.app.me.kapooktest.helper.LineHelper;
import com.app.me.kapooktest.helper.ShareHandle;
import com.app.me.kapooktest.modelclass.AllContentModel;
import com.app.me.kapooktest.modelclass.ConstantModel;
import com.app.me.kapooktest.modelclass.HotContentModel;
import com.app.me.kapooktest.modelclass.NewContentModel;
import com.app.me.kapooktest.modelclass.PortalHomeModel;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener   {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "SuRiVCurse";
    private static final String TWITTER_SECRET = "miragezii@hotmail.com";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
private Context context;
    /**
     * The {@link ViewPager} that will host the section contents.
     */

    private CustomViewPager mViewPager;
    private AQuery aQuery;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private static final String FIREBASE_SERVER_KEY = "AAAACLcrnik:APA91bEI52Wyz49OwoS6VNVb-U4NZlmU_qhDDhCJoDB0hfWukdpmKhW6xzM4fxiXA53AT62CYkLdRDmk3b4vsetdseVwMLF_PKtNkDhnpskOOla4nemnCRDiFzACPgw8VIcg7zP_k0hH";
    private AccessToken facebookAccesstoken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        //Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_hamberger);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        aQuery = new AQuery(this);
        context = this;



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        facebookAccesstoken = getAccessToken();
        this.drawerHambergerView();
        this.runFireBase();

        // Set up the ViewPager with the sections adapter.
        mViewPager = (CustomViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPagingEnabled(false);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(mViewPager);

        createFloatingAction();



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent addTabIntent = new Intent(context,ManageTabActivity.class);
            startActivity(addTabIntent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshNavigationItem(){
        final Menu menu = navigationView.getMenu();

        if(!ConstantModel.IS_Tabs_CHANGE){
            ConstantModel.TabsModel tab =  ConstantModel.getTabsModel(ConstantModel.getSizeListTabs()-1);
            menu.removeGroup(R.id.item_group);
            //menu.add(R.id.item_group,Menu.NONE,1,"");
                    //Drawable icon = null;
            addNavigationItem(menu,ConstantModel.getAllTabs());

        }
    }

    private void addNavigationItem(Menu menu, ArrayList<ConstantModel.TabsModel> tabsModels ){
        int i=0;
        for (ConstantModel.TabsModel tab: tabsModels) {
            Drawable icon = null;
            if(tab.getIcon() > 0){
                icon =  getResources().getDrawable(tab.getIcon(),getTheme());
            }

            addNavigationItem(menu,tab.getNameTabs(),icon,i++);
        }
    }

    private void addNavigationItem(Menu menu, CharSequence title,Drawable icon ,int _id){
        MenuItem menuItem =  menu.add(R.id.item_group,_id,Menu.FLAG_APPEND_TO_GROUP,title);


        if(icon != null){
            menuItem.setIcon(icon);
        }else{
            menuItem.setIcon(R.drawable.ic_favorite_black);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id){
            case R.id.howtoView:
                Intent howtoViewActivity = new Intent(this.getApplicationContext() ,HowtoViewActivity.class);
                startActivityForResult (howtoViewActivity,0);
                return true;
            case R.id.entryView:
                Intent entryViewActivity = new Intent(this.getApplicationContext() ,EntryViewActivity.class);
                startActivityForResult (entryViewActivity,0);
                return true;
            default:
                mViewPager.setCurrentItem(id);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            ConstantModel.refreshTabs(getApplicationContext());
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return ConstantModel.getSizeListTabs();
        }



        @Override
        public CharSequence getPageTitle(int position) {
            return ConstantModel.getListTabsKey().get(position);
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
            super.restoreState(state, loader);
            ConstantModel.refreshTabs(getApplicationContext());

        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            super.registerDataSetObserver(observer);

        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        private View rootView;
        private AQuery aq;
        private static final Gson gson = new GsonBuilder().create();
        private VertialRecAdapter vertialRecAdapter;
        private SwipeRefreshLayout swipeContainer;

        protected LayoutManagerType mCurrentLayoutManagerType;
        private ProgressDialog mLoading;

        public CallbackManager callbackManager;
        public ShareDialog shareDialog;

        private static final int DATA_LENGTH = 8;

        private enum LayoutManagerType {
            GRID_LAYOUT_MANAGER,
            LINEAR_LAYOUT_MANAGER
        }
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static JsonHelper JSON_HELPER;

        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {


            Bundle b = getArguments();
            int sectionNumber = b.getInt(ARG_SECTION_NUMBER);

            ConstantModel.TabsModel model = ConstantModel.getTabsModel(sectionNumber);
            //Class model = ConstantModel.getTabsModelClass(sectionNumber);
            //JSON_HELPER = new JsonHelper(model);
           // Object result = JSON_HELPER.runGetJson();
            Log.d("Runing ",""+sectionNumber);

            switch(sectionNumber){
                case 0:
                    rootView = inflater.inflate(R.layout.fragment_main, container, false);
                    aq = new AQuery(rootView);
                    final RecyclerView recContainer = (RecyclerView)rootView.findViewById(R.id.verticalContainer);
                    mCurrentLayoutManagerType = LayoutManagerType.LINEAR_LAYOUT_MANAGER;
                    aq.ajax(model.getJsonUrl(),JSONObject.class ,0,new AjaxCallback<JSONObject>(){
                        @Override
                        public void callback(String url, JSONObject object, AjaxStatus status) {
                            super.callback(url, object, status);

                            if(object != null){

                                PortalHomeModel result = gson.fromJson(object.toString(), PortalHomeModel.class);

                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                                mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                vertialRecAdapter = new VertialRecAdapter(result);

                                recContainer.setHasFixedSize(true);
                                recContainer.setLayoutManager(mLayoutManager);
                                recContainer.setAdapter(vertialRecAdapter);
                                recContainer.setRecycledViewPool(new RecyclerView.RecycledViewPool());

                                Log.d("Runing ",""+url+" Success");
                            }else{
                                //ajax error
                                Log.d("Runing error",""+url);
                            }
                        }
                    });
                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.layout_content, container, false);
                    swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
                    mLoading = new ProgressDialog(rootView.getContext());
                    mLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mLoading.setMessage("Loading....");
                    final RecyclerView recAllContent = (RecyclerView) rootView.findViewById(R.id.recAllContent);

                    recAllContent.smoothScrollToPosition(0);
                    recAllContent.setHasFixedSize(true);
                    final GridLayoutManager mLayoutManager = new GridLayoutManager(rootView.getContext(), 1);
                    recAllContent.setLayoutManager(mLayoutManager);

                    aq = new AQuery(rootView);

                    aq.ajax(ConstantModel.getTabsModel(1).getJsonUrl(),JSONArray.class ,new AjaxCallback<JSONArray>(){
                        @Override
                        public void callback(String url, JSONArray object, AjaxStatus status) {
                            super.callback(url, object, status);

                            if(object != null){

                                final AllContentModel[] contentResult  = gson.fromJson(object.toString(), AllContentModel[].class);
                                final ArrayList<AllContentModel> allNewContent = new ArrayList<>(Arrays.asList(contentResult).subList(0, DATA_LENGTH));
                                final AllContentAdapter newContentAdapter = new AllContentAdapter(allNewContent);
                                final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
                                    @Override
                                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {


                                        final int itemStart = totalItemsCount;
                                        final int itemEnd = totalItemsCount < contentResult.length-(DATA_LENGTH +1) ? itemStart+ DATA_LENGTH : contentResult.length-1;

                                        final int curSize = newContentAdapter.getItemCount();
                                        Log.d("View", "EndlessRecyclerViewScrollListener onLoadMore: "+curSize);
                                        if (curSize == totalItemsCount) {

                                            ArrayList<AllContentModel> moreContents = new ArrayList<>(Arrays.asList(contentResult).subList(itemStart,itemEnd));

                                            allNewContent.addAll(moreContents);
                                        }
                                    }
                                };

                                recAllContent.setAdapter(newContentAdapter);
                                recAllContent.addOnScrollListener(scrollListener);
                                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {
                                        scrollListener.resetState();
                                        allNewContent.clear();
                                        allNewContent.addAll(new ArrayList<>(Arrays.asList(contentResult).subList(0, DATA_LENGTH)));
                                        //newContentAdapter.notifyDataSetChanged();

                                        swipeContainer.setRefreshing(false);
                                        //fetchTimelineAsync(0);
                                    }
                                });

                                Log.d("Runing ",""+url+" Success");
                            }else{
                                //ajax error
                                Log.d("Runing error",""+url);
                            }
                        }
                    });

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.layout_hotcontent, container, false);
                    swipeContainer = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);

                    mLoading = new ProgressDialog(rootView.getContext());
                    mLoading.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mLoading.setMessage("Loading....");
                    final GridLayoutManager mLayoutManager2 = new GridLayoutManager(rootView.getContext(), 1);
                    final RecyclerView recHotContent = (RecyclerView) rootView.findViewById(R.id.recHotContent);

                    recHotContent.setHasFixedSize(true);
                    recHotContent.setLayoutManager(mLayoutManager2);
                    aq = new AQuery(rootView);
                    aq.ajax(model.getJsonUrl(),JSONArray.class ,new AjaxCallback<JSONArray>(){
                        @Override
                        public void callback(String url, JSONArray object, AjaxStatus status) {
                            super.callback(url, object, status);

                            if(object != null){
                                final HotContentModel[] hotContentResult = gson.fromJson(object.toString(), HotContentModel[].class);
                                final ArrayList<HotContentModel> allHotContent = new ArrayList<>(Arrays.asList(hotContentResult).subList(0, DATA_LENGTH));
                                final HotContentAdapter hotContentAdapter = new HotContentAdapter(allHotContent);


                                final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager2) {
                                    @Override
                                    public void onLoadMore(int page, final int totalItemsCount, RecyclerView view) {

                                        final int itemStart = totalItemsCount;
                                        final int itemEnd = totalItemsCount < hotContentResult.length-(DATA_LENGTH +1) ? itemStart+ DATA_LENGTH : hotContentResult.length-1;

                                        final int curSize = hotContentAdapter.getItemCount();
                                        Log.d("Runing","page = "+page+ " curSize = "+curSize+" totalItemsCount = "+totalItemsCount+" itemStart = "+itemStart);
                                        if ((curSize == totalItemsCount)) {
                                            ArrayList<HotContentModel> moreContents = new ArrayList<>(Arrays.asList(hotContentResult).subList(itemStart,itemEnd));
                                            allHotContent.addAll(moreContents);


                                        }
                                    }
                                };

                                recHotContent.setAdapter(hotContentAdapter);
                                recHotContent.addOnScrollListener(scrollListener);
                                swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                    @Override
                                    public void onRefresh() {
                                        scrollListener.resetState();
                                        allHotContent.clear();
                                        allHotContent.addAll(new ArrayList<>(Arrays.asList(hotContentResult).subList(0, DATA_LENGTH)));
                                        //hotContentAdapter.notifyDataSetChanged();

                                        swipeContainer.setRefreshing(false);

                                    }
                                });
                                Log.d("Runing ",""+url+" Success");
                            }else{
                                //ajax error
                                Log.d("Runing error",""+url);
                            }
                        }
                    });
                    break;
            }
            return rootView;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConstantModel.refreshTabs(getApplicationContext());
        refreshNavigationItem();
        mSectionsPagerAdapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Login", "onDestroy: accessTokenStopTracker();");
        accessTokenStopTracker();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("LOGIN", "onActivityResult: ");
        //getCallbackManager().onActivityResult(requestCode, resultCode, data);
        //Log.d("onActivityResult", "onActivityResult: ");
    }

    private void runFireBase(){
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference myRef = database.child("condition");
// Read from the database

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Toast.makeText(getBaseContext(),value,Toast.LENGTH_SHORT).show();
                Log.d("RealDatabase", "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("RealDatabase", "Failed to read value.", error.toException());
            }
        });

        FirebaseMessaging.getInstance().subscribeToTopic("hots");
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String token = FirebaseInstanceId.getInstance().getToken();
        String msg = getString(R.string.msg_token_fmt, token);

        String url = "https://iid.googleapis.com/iid/info/"+token+"?details=true";
        AjaxCallback<JSONObject> aJaxCallBack = new AjaxCallback<JSONObject>(){
            @Override
            public void callback(String url, JSONObject object, AjaxStatus status) {
                super.callback(url, object, status);
                JsonParser jsonParser = new JsonParser();
                if(object != null){
                    Gson gson = new Gson();
                    JsonObject topicList = null;
                    try {
                        topicList = (JsonObject) jsonParser.parse(object.getJSONObject("rel").getJSONObject("topics").toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HashMap<String,String> topicHashList = gson.fromJson(topicList,HashMap.class);

                    if(topicList!=null){

                        Log.d("topicList", "callback: topicList "+gson.toJson(topicHashList));

                    }
                }
            }
        };
        aJaxCallBack.header("Authorization", "key="+FIREBASE_SERVER_KEY);
        if(token != null){
            aQuery.ajax(url,JSONObject.class,aJaxCallBack);
        }
        Log.d("ServiceMain", msg);
    }
    private void drawerHambergerView(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final Menu menu = navigationView.getMenu();

        addNavigationItem(menu,ConstantModel.getAllTabs());
        createHeaderHamberger(navigationView.getHeaderView(0));
    }
    private void createHeaderHamberger(View navigationHeaderView){
        final Button btnShowLogin = (Button) navigationHeaderView.findViewById(R.id.btnShowLogin);
        final ImageView imgProfile = (ImageView) navigationHeaderView.findViewById(R.id.imgProfile);
        final TextView txtProfileName = (TextView) navigationHeaderView.findViewById(R.id.txtProfileName);
        //final Bitmap defaultProfile = imgProfile.getDrawingCache();
        btnShowLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnShowLogin.getText().equals(getString(R.string.sign_in))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }
                else{
                    btnShowLogin.setText(getString(R.string.sign_in));
                    txtProfileName.setVisibility(View.GONE);
                    imgProfile.setImageResource(R.mipmap.ic_kapook);
                    if(facebookAccesstoken != null){
                        facebookAccesstoken = null;
                        FacebookHelper.logout();
                    }

                    if(LineHelper.checkStatusLineLogin(getApplicationContext())){
                        LineHelper.lineLogout();
                    }
                }

            }
        });

        if(facebookAccesstoken != null){
            Log.d("LOGIN", "createHeaderHamberger: loadUserProfile"+facebookAccesstoken.getUserId());
            FacebookHelper.loadUserProfile(facebookAccesstoken,navigationHeaderView);
            FacebookHelper.loadProfilePicture(facebookAccesstoken,navigationHeaderView);
        }
        else if(LineHelper.checkStatusLineLogin(getApplicationContext())){
            LineHelper.loadProfileUser(getApplicationContext(),navigationHeaderView);
        }
    }


    private void createFloatingAction() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBottomSheet();
            }
        });
    }

    private  void openBottomSheet() {

        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_main_menu, null);

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.layout_menu_bottom));

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        mBottomSheetDialog.setContentView (view);
        mBottomSheetDialog.setCancelable (true);
        mBottomSheetDialog.show ();

        LinearLayout layout_howto = (LinearLayout) view.findViewById(R.id.bottom_sheet_howTo);
        layout_howto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChoiceCategory = new Intent(context,ChoiceCategoryActivity.class);
                intentChoiceCategory.putExtra("INTENT_ID",1);
                startActivity(intentChoiceCategory);
            }
        });

        LinearLayout layout_entry = (LinearLayout) view.findViewById(R.id.bottom_sheet_entry);
        layout_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentChoiceCategory = new Intent(context,ChoiceCategoryActivity.class);
                intentChoiceCategory.putExtra("INTENT_ID",2);
                startActivity(intentChoiceCategory);
            }
        });
    }
}
