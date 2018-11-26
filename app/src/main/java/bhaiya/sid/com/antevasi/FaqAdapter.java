package bhaiya.sid.com.antevasi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {
    Context context;
    View view;
    ArrayList<FaqResponse> faqList;
    public FaqAdapter(Context context,ArrayList<FaqResponse> faqList){
        this.context = context;
        this.faqList = faqList;
    }

    @Override
    public FaqAdapter.FaqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        view = inflater.from(parent.getContext()).inflate(R.layout.single_faq_item,parent,false);
        FaqAdapter.FaqViewHolder faqViewHolder= new FaqAdapter.FaqViewHolder(view);
        return faqViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FaqAdapter.FaqViewHolder holder, int position) {
        final FaqResponse  FaqResponse = faqList.get(position);
        String askedBy="Asked by : "+FaqResponse.getName(); //name is concatenated by asked by
        holder.name.setText(askedBy);
        holder.question.setText(FaqResponse.getQuestion());
        holder.answer.setText(FaqResponse.getAnswer());
    }


    @Override
    public int getItemCount() {
        return faqList.size();
    }

    class FaqViewHolder extends RecyclerView.ViewHolder{
        TextView name, question, answer;
        public FaqViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            question=itemView.findViewById(R.id.question);
            answer=itemView.findViewById(R.id.answer);

        }
    }
}
