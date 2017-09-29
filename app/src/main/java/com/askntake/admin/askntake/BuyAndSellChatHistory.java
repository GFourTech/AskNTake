package com.askntake.admin.askntake;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.ChatHistoryAdapter;
import Pojo.OwnerHistoryPojo;
import AppUtils.ServiceHandler;

/**
 * Created by admin on 4/6/2017.
 */

public class BuyAndSellChatHistory extends AppCompatActivity {
    WebView webView;
    String showpage = null;
    ArrayList<OwnerHistoryPojo> OwnerHistory;

    //ImageView back_button,popup_icon;

    ListView chathystory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buy_and_sell_chat_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chathystory=(ListView)findViewById(R.id.chathystory);

        OwnerHistory= new ArrayList<OwnerHistoryPojo>();
       new  PopulateOwnerHistorysAsynchTask().execute();
    }


    private class PopulateOwnerHistorysAsynchTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... params) {

            ServiceHandler serviceHandler=new ServiceHandler();
            String response= serviceHandler.makeServiceCall("http://www.askntake.com/api/rest/chat/getappOwnerChatHistory","POST",getRequestObject());
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response!=null) {
                OwnerHistory.clear();
                String message="";
                String messageDate="";
                String status="";
                String userName="";
                String productId="";
                String productName="";
                String productImage="";
                String profileImage="";
                String productColor="#000000";
                int last_messageId=0;
                try {
                    JSONObject mainobj= new JSONObject(response);
                    JSONArray historyArray= mainobj.getJSONArray("history");
                    for (int i = 0; i < historyArray.length(); i++) {
                        JSONObject obj= historyArray.getJSONObject(i);

                        JSONObject messageObj= obj.getJSONObject("lastMessage");
                        message=messageObj.getString("message");
                        messageDate=messageObj.getString("messageDate");
                        status=messageObj.getString("status");
                        last_messageId=messageObj.getInt("messageId");


                        productId=obj.getString("productId");
                        productName=obj.getString("productName");
                        productImage=obj.getString("productImage");

                        userName=obj.getString("userName");
                        String userId=obj.getString("userId");

                        profileImage=obj.getString("userImage");

                        productColor=obj.getString("productColor");
                        String pasroductColor=productColor;
                        int unread_messages=obj.getInt("unredMessages");
                      //ssss
                       /* if(unread_messages==0)
                        {
                            ShortcutBadger.removeCount(ChatHistory.this);
                        }*/



                        OwnerHistoryPojo pojo = new OwnerHistoryPojo();
                        pojo.setLastMessage(message);
                        pojo.setStatus(status);
                        pojo.setUserName(userName);
                        pojo.setUseId(userId);
                        pojo.setProductId(productId);
                        pojo.setProductName(productName);
                        pojo.setProductImage(productImage);
                        pojo.setProfile_image(profileImage);
                        pojo.setProductColor(productColor);
                        pojo.setUnreadmessages(unread_messages);
                        pojo.setLastmessageid(last_messageId);
                        OwnerHistory.add(pojo);

                    }


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    try {
                        JSONObject mainobj= new JSONObject(response);

                        JSONObject obj= mainobj.getJSONObject("history");

                        JSONObject messageObj= obj.getJSONObject("lastMessage");
                        message=messageObj.getString("message");
                        messageDate=messageObj.getString("messageDate");
                        status=messageObj.getString("status");
                        last_messageId=messageObj.getInt("messageId");


                        productId=obj.getString("productId");
                        productName=obj.getString("productName");
                        productImage=obj.getString("productImage");

                        userName=obj.getString("userName");
                        String userId=obj.getString("userId");

                        profileImage=obj.getString("userImage");
                        productColor=obj.getString("productColor");
                        int unread_messages=obj.getInt("unredMessages");
                       ///sssss
                        /*if(unread_messages==0)
                        {
                            ShortcutBadger.removeCount(ChatHistory.this);
                        }
*/

                        OwnerHistoryPojo pojo = new OwnerHistoryPojo();
                        pojo.setLastMessage(message);
                        pojo.setStatus(status);
                        pojo.setUserName(userName);
                        pojo.setUseId(userId);
                        pojo.setProductId(productId);
                        pojo.setProductName(productName);
                        pojo.setProductImage(productImage);
                        pojo.setProfile_image(profileImage);
                        pojo.setProductColor(productColor);
                        pojo.setUnreadmessages(unread_messages);
                        pojo.setLastmessageid(last_messageId);

                        OwnerHistory.add(pojo);
                    } catch (JSONException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
            if (OwnerHistory.size() > 0) {

              //  no_chat_layout.setVisibility(View.GONE);
                //chatLV.setVisibility(View.VISIBLE);
                ChatHistoryAdapter chatAdapater = new ChatHistoryAdapter(BuyAndSellChatHistory.this, OwnerHistory,"product");
                chathystory.setAdapter(chatAdapater);
            }
            else
            {
               // chatLV.setVisibility(View.GONE);
               // no_chat_layout.setVisibility(View.VISIBLE);
            }
        }


    }

    private JSONObject getRequestObject()
    {


        JSONObject jsonObject=null ;

        try {
            jsonObject = new JSONObject();

            jsonObject.accumulate("senderRegId", "1");
            jsonObject.accumulate("reciverRegId", "");
            jsonObject.accumulate("itemId", "");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return  jsonObject;

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