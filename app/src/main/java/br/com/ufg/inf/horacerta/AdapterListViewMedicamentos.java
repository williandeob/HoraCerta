package br.com.ufg.inf.horacerta;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import model.Medicamento;

/**
 * Created by willian on 30/06/2016.
 */
public class AdapterListViewMedicamentos extends BaseAdapter{
    private LayoutInflater mInflater;
    private List<Medicamento> itens;
    Context context;

    public AdapterListViewMedicamentos(Context context, List<Medicamento> itens) {
        this.itens = itens;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public Context getContext(){
        return this.context;
    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Object getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemSuporte itemHolder;
        //se a view estiver nula (nunca criada), inflamos o layout nela.
        if (convertView == null) {
            //infla o layout para podermos pegar as views
            convertView = mInflater.inflate(R.layout.medicamento_item, null);

            //cria um item de suporte para não precisarmos sempre
            //inflar as mesmas informacoes
            itemHolder = new ItemSuporte();
            itemHolder.nome = ((TextView) convertView.findViewById(R.id.nomeMedicamento));
            itemHolder.image = ((ImageView) convertView.findViewById(R.id.imagemMedicamento));

            //define os itens na view;
            convertView.setTag(itemHolder);
        } else {
            //se a view já existe pega os itens.
            itemHolder = (ItemSuporte) convertView.getTag();
        }

        //pega os dados da lista
        //e define os valores nos itens.
        Medicamento item = itens.get(position);
        itemHolder.nome.setText(item.getNome());
        if(item.getImagem() == null) {
            itemHolder.image.setImageResource(R.drawable.ic_error_outline_black_48dp);
        }else{
            Bitmap bitmap = BitmapFactory.decodeByteArray(item.getImagem(), 0, item.getImagem().length);
            itemHolder.image.setImageBitmap(bitmap);
        }

        //retorna a view com as informações
        return convertView;
    }

    /**
     * Classe de suporte para os itens do layout.
     */
    private class ItemSuporte {

        ImageView image;
        TextView nome;
    }
}
