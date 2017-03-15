package com.example.patricioeinstein.peritagem;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;


public class UserRecyclerAdapter extends FirebaseRecyclerAdapter<Utilizador, UserViewHolder> {

    public UserRecyclerAdapter(
            Class<Utilizador> modelClass,
            int modelLayout,
            Class<UserViewHolder> viewHolderClass,
            Query ref ){
        super( modelClass, modelLayout, viewHolderClass, ref );
    }
    @Override
    protected void populateViewHolder(
            UserViewHolder userViewHolder,
            Utilizador utilizador, int i) {
        userViewHolder.text1.setText( utilizador.getName() );
        userViewHolder.text2.setText( utilizador.getEmail() );
    }
}