package com.askntake.admin.askntake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Adapters.CatogiryGridDisplyAdapter;
import Pojo.FilterPojo;
import Pojo.ProductItemsPojo;

/**
 * Created by admin on 4/11/2017.
 */

public class SearchCategoriesActivity extends AppCompatActivity {
    GridView catogiry_filtering_grid;
    ArrayList<ProductItemsPojo> productItemsListData = new ArrayList<ProductItemsPojo>();
    ImageView back_button;

    public Integer[] mThumbIds = {
            R.drawable.catogiry1, R.drawable.catogiry2,
            R.drawable.catogiry3, R.drawable.catogiry4,
            R.drawable.catogiry5, R.drawable.catogiry6,
            R.drawable.catogiry7, R.drawable.catogiry8,
            R.drawable.catogiry9, R.drawable.catogiry10,
            R.drawable.catogiry11
    };
    public String[] CatogiryNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buyandsell_filtering);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FilterPojo  filterPojo = (FilterPojo) getIntent().getSerializableExtra("filterdata");

        CatogiryNames=getApplicationContext().getResources().getStringArray(R.array.categories_list);
        catogiry_filtering_grid=(GridView)findViewById(R.id.filtering_grid);
        catogiry_filtering_grid.setAdapter(new CatogiryGridDisplyAdapter(this,mThumbIds,CatogiryNames,filterPojo));

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
