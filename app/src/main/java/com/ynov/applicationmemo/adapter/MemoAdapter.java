package com.ynov.applicationmemo.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.ynov.applicationmemo.R;

import java.util.List;

import com.ynov.applicationmemo.dto.MemoDTO;
import com.ynov.applicationmemo.entity.Memo;
import com.ynov.applicationmemo.viewHolder.MemoViewHolder;

public class MemoAdapter extends RecyclerView.Adapter<MemoViewHolder>
{
    // Liste d'objets métier :
    private List<MemoDTO> listeMemos = null;


    /**
     * Constructeur.
     * @param listeMemos Liste de mémos
     */
    public MemoAdapter(List<MemoDTO> listeMemos)
    {
        this.listeMemos = listeMemos;
    }

    @Override
    public MemoViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View viewMemo = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_memo, parent, false);
        return new MemoViewHolder(viewMemo);
    }

    @Override
    public void onBindViewHolder(MemoViewHolder holder, int position)
    {
        holder.getTextViewIntitule().setText(listeMemos.get(position).getIntitule());
    }

    @Override
    public int getItemCount()
    {
        return listeMemos.size();
    }

    /**
     * Ajout d'un mémo à la liste.
     * @param memo Mémo
     */
    public void ajouterMemo(MemoDTO memo)
    {
        listeMemos.add(0, memo);
        notifyItemInserted(0);
    }

    public MemoDTO getItemParPosition(int position) {
        return listeMemos.get(position);
    }
}