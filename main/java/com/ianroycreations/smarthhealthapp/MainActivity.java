package com.ianroycreations.smarthhealthapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private String[] infoSegments;

    private List<String> stringList;

    private GoogleApiClient googleApiClient;

    private Uri urlFoto;
    private String usuarioIdCuenta;
    private String miNombre;

    private SignInButton signInButton;
    public static final int SIGN_IN_CODE = 777;

//TODO: membrete parte superior ver si al hacer un intent a otra activity se pone el nombre de la activity y en ese caso apicarlo a analysed movements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stringList=new ArrayList<>();


        //Google login config
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        setContentView(R.layout.activity_main);
        checkLogged();



        /*
        MenuItem menuItem=(MenuItem) findViewById(R.id.nav_analysed);
        menuItem.setChecked(true);
        */


        //Navigation bar
        makeDrawer();

/*

        //Set default selected on menu

        NavigationView navigation_view = (NavigationView) findViewById(R.id.nav_view);
        navigation_view.setCheckedItem(R.id.nav_analysed);
*/


/*
        //RECYCLER VIEW
        mRecyclerView=(RecyclerView)findViewById(R.id.recycler_view_layour_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //division
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getApplicationContext()
        ));
        //ends division

        TextAdapter textAdapter=new TextAdapter();
        mRecyclerView.setAdapter(textAdapter);


        stringList.add("Day 1");
        stringList.add("Day 1.2");
        stringList.add("Day 2");
        stringList.add("Day x");
        stringList.add("Day x");
        stringList.add("Day x");
        stringList.add("Day x");
        stringList.add("Day x");
        stringList.add("Day x");
        stringList.add("Day x");

        textAdapter.setItems(stringList);
*/


        //Button +
        buttonPlus();


        /*
        //Button START
        Button buttonStart=findViewById(R.id.button);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRecyclerView();
            }
        });
*/


    }

    private void buttonPlus() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDataFile();
            }
        });
    }

    private void makeDrawer() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void addNewDataFile() {

        Fragment addFile=new AddFile();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayoutMain, addFile).commit();


    }

    private void setRecyclerView(){
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDataFile();
            }
        });

        Fragment analaysedMovements=new AnalysedMovements();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayoutMain, analaysedMovements).commit();
    }

    private void checkLogged() {

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
            setContentView(R.layout.activity_main);
            makeDrawer();
            buttonPlus();
            setRecyclerView();
        } else {
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount account = result.getSignInAccount();

            miNombre = account.getDisplayName();
            usuarioIdCuenta=account.getId();

            //yo = new Usuario(miNombre,7,usuarioIdCuenta);
            //subirAFirebase(yo,"Usuarios/"+yo.getUsuarioId());

            urlFoto = account.getPhotoUrl();


            setRecyclerView();

        } else {
            goLogInScreen();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int menuId = item.getItemId();

        if (menuId == R.id.nav_analysed) {
            setRecyclerView();
        } else if (menuId == R.id.nav_record) {
            pushRecord();
        }  else if (menuId == R.id.nav_manage) {

        } else if (menuId == R.id.nav_share) {

        } else if (menuId == R.id.nav_send) {
            logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void pushRecord() {
        Fragment record= new Record();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayoutMain, record).commit();
    }

    private void goLogInScreen() {
        /*
        Fragment logInFragment=new LogInFragment();
        FragmentManager fragmentManager=getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.relativeLayoutMain, logInFragment).commit();
*/

        //TODO: Volver a setcontentview(main)?
        setContentView(R.layout.fragment_log_in);
        signInButton = (SignInButton) findViewById(R.id.signInButton);

        signInButton.setSize(SignInButton.SIZE_WIDE);

        signInButton.setColorScheme(SignInButton.COLOR_DARK);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, SIGN_IN_CODE);
            }
        });
        //setContentView(R.layout.activity_main);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult2(result);
        }
    }

    //After haved logged in:
    private void handleSignInResult2(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //What is done after the first logIn:
            setContentView(R.layout.activity_main);
            makeDrawer();
            buttonPlus();
            setRecyclerView();
        } else {
            Toast.makeText(this, "not log in", Toast.LENGTH_SHORT).show();
        }
    }

    public void logOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()) {
                    goLogInScreen();
                } else {
                    Toast.makeText(getApplicationContext(), "session not closed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
