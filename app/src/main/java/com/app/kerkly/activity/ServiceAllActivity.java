package com.app.kerkly.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.kerkly.R;
import com.app.kerkly.adepter.ServiceAdapter;
import com.app.kerkly.model.SubcatDatum;
import com.app.kerkly.utils.SessionManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceAllActivity extends BasicActivity implements ServiceAdapter.RecyclerTouchListener {

    @BindView(R.id.recycler_service)
    RecyclerView recyclerService;
    ArrayList<SubcatDatum> subcatDataItems=new ArrayList<>();
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_all);
        ButterKnife.bind(this);
        getSupportActionBar().setTitle("View All Service");
        subcatDataItems = getIntent().getParcelableArrayListExtra("ListExtra");
        sessionManager=new SessionManager(this);
        ServiceAdapter itemAdp = new ServiceAdapter( subcatDataItems ,this, "viewall");
        recyclerService.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerService.setAdapter(itemAdp);
    }

    @Override
    public void onClickServiceItem(SubcatDatum dataItem, int position) {
        sessionManager.setStringData("pid","0");
        startActivity(new Intent(this, SubCategoryActivity.class)
                .putExtra("vurl",dataItem.getVideo())
                .putExtra("name", dataItem.getTitle())
                .putExtra("named", dataItem.getSubtitle())
                .putExtra("cid",subcatDataItems.get(0).getCatid())
                .putExtra("sid",dataItem.getSubcatId()));
    }
}