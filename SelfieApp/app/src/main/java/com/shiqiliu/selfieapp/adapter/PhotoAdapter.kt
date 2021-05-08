package com.shiqiliu.selfieapp.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shiqiliu.selfieapp.R
import com.shiqiliu.selfieapp.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.row_photo_adapter.view.*

class PhotoAdapter (var mContext:Context) : RecyclerView.Adapter<PhotoAdapter.MyViewHolder>(){
    var mList:ArrayList<String> = ArrayList()
    inner class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
        fun bind(uri:String){
            var temp = Uri.parse(uri)//covert string to uri
            Log.d("abc","Enter bind view")
            Picasso.get()
                    .load(temp)
                //.fit().centerCrop()
                    .into(itemView.image_view_photo)
            Log.d("abc","adapter uri:$uri")
            Log.d("abc","adapter uri:$temp")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(mContext).inflate(R.layout.row_photo_adapter,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       var uri = mList[position]
        return holder.bind(uri)
    }

    override fun getItemCount(): Int {
    return mList.size
    }
    fun setData(user: User){
        mList = user.imageUri!!
        notifyDataSetChanged()
    }
}