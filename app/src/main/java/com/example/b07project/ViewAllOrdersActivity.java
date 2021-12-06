package com.example.b07project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewAllOrdersActivity extends AppCompatActivity {
    
    private ArrayAdapter<CustomerOrder> ordersAdapter;
    private ListView listView;
    private StoreOwner owner;

    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_orders);

        listView = findViewById(R.id.listView);
        owner = (StoreOwner) getIntent().getSerializableExtra("account");

        ordersAdapter = new ArrayAdapter<CustomerOrder>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(ordersAdapter);

        //display the orders on the listview
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("store owners").child(owner.username).child("Orders");
        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Log.e("B07 Project", "Couldn't get data", task.getException());
                }
                else{
                    for(DataSnapshot child : task.getResult().getChildren()) {
                        CustomerOrder order = child.getValue(CustomerOrder.class);

                        //make sure it's not an empty order
                        if (order.items != null) {
                            ordersAdapter.add(order);
                        }
                    }
                }
            }
        });

//        ValueEventListener listener = new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                owner.wipeOrders();
//                owner.populateOrders();
//                display();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Context context = getApplicationContext();
//                Toast.makeText(context,"Error getting data", Toast.LENGTH_LONG).show();
//            }
//        };
        setUpListViewListener();

    }

//    private void display(){
//        ordersAdapter.clear();
//        if(!owner.orders.isEmpty()) {
//            for (CustomerOrder order : owner.orders) {
//                String toDisplay = "Ordered by " + order.customer + ", Order" + order.toString();
//                ordersAdapter.add(toDisplay);
//            }
//        }
//    }

    private void setUpListViewListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //get position to use on owner.orders.get(postion)
                //remove from the owner orders list and write it
                //go to the customer in the order and set its boolean completed to true

                //StoreOwner owner = (StoreOwner) getIntent().getSerializableExtra("account");
                //DatabaseReference ref = FirebaseDatabase.getInstance().getReference("store owners");
                Context context = getApplicationContext();
                Toast.makeText(context,"Order Completed!",Toast.LENGTH_LONG).show();

                owner.removeAndSetComplete(position, ordersAdapter); //removes from owner.orders and writes it
                //CustomerOrder order = owner.orders.get(position);
                //String customer = order.customer;

                //owner.orders.remove(position);
                //UpdateCustomerOrder(order,customer);


                //ref.child(owner.getUsername()).child("Orders").setValue(owner.orders);
//              ordersAdapter.notifyDataSetChanged();
                Intent intent = new Intent(ViewAllOrdersActivity.this, OwnerLanding.class);
                intent.putExtra("account", owner);
                startActivity(intent);

                return true;

            }
        });

    }

//    public void UpdateCustomerOrder(CustomerOrder order, String customer){
//        DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference("customers");
//        ref2.child(customer).child("Orders").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if (!(task.isSuccessful())) {
//                    //error
//                } else {
//                    if (task.getResult().hasChildren()) {
//
//                        int i = 0;
//                        for (DataSnapshot child : task.getResult().getChildren()) {
//                            CustomerOrder order2 = child.getValue(CustomerOrder.class);
//                            if (order2 != null) {
//                                if (order.orderNumber == order2.orderNumber) {
//                                    order2.markComplete();
//                                    ref2.child(customer).child("Orders").child((String.valueOf(i))).setValue(order2);
//                                }
//                            }
//                            i++;
//                        }
//                    }
//                }
//            }
//
//        });
//    }


    private String formatOrder(CustomerOrder order){
        String output = "OrderNumber: " + order.orderNumber + ", From: " + order.customer + '\n';
        for (Product p: order.items){
            output += p.toString() + '\n';
        }
        output+= "Completed: " + order.completed;
        return output;
    }
}