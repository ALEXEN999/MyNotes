package com.alexen.mynotes.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.alexen.mynotes.R;
import com.alexen.mynotes.model.Note;
import com.alexen.mynotes.viewmodel.MyNotesViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Field;

import io.realm.RealmResults;

public class HomeFragment extends Fragment {

    private NavController navController;
    MyNotesViewModel myNotesViewModel;
    private MyNotesAdapter myNotesAdapter;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        myNotesViewModel = ViewModelProviders.of(requireActivity()).get(MyNotesViewModel.class);

        searchView = view.findViewById(R.id.search_view);

        setSearchTextColour();
        setSearchIcons();

        fab = view.findViewById(R.id.fab_new_note);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.newNoteFragment);
            }
        });

        recyclerView = view.findViewById(R.id.recycler_listaNotes);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        myNotesAdapter = new MyNotesAdapter();

        myNotesAdapter.establecerListaTareas(myNotesViewModel.obtenerNotasDetalle());
        recyclerView.setAdapter(myNotesAdapter);

        searchView = view.findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RealmResults<Note> notes = newText.equals("") ? myNotesViewModel.obtenerNotasDetalle() : myNotesViewModel.obtenerNotasDetallePorNombre(newText);
                myNotesAdapter.establecerListaTareas(notes);
                recyclerView.setAdapter(myNotesAdapter);

                return true;
            }
        });

    }

    class MyNotesAdapter extends RecyclerView.Adapter<MyNotesAdapter.TareaViewHolder>{

        RealmResults<Note> tareaDetalleList;



        @NonNull
        @Override
        public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TareaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_note, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
            final Note note = tareaDetalleList.get(position);
            Log.i("Logger", String.valueOf(note.getId()));
            holder.titleTextView.setText(note.getTitle());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myNotesViewModel.editarNota(note.getId());
                    navController.navigate(R.id.newNoteFragment);
                }
            });
        }

        @Override
        public int getItemCount() {
            return tareaDetalleList != null ? tareaDetalleList.size() : 0;
        }

        void establecerListaTareas(RealmResults<Note> list){
            tareaDetalleList = list;
            notifyDataSetChanged();
        }

        class TareaViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;

            TareaViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.textViewTitle);

            }
        }
    }

    private void setSearchTextColour() {
        int searchPlateId = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = (EditText) searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getActivity().getResources().getColor(R.color.white));
        searchPlate.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    private void setSearchIcons() {
        try {
            Field searchField = SearchView.class.getDeclaredField("mCloseButton");
            searchField.setAccessible(true);
            ImageView closeBtn = (ImageView) searchField.get(searchView);
            closeBtn.setImageResource(R.drawable.ic_close);


        } catch (NoSuchFieldException e) {
            Log.e("SearchView", e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Log.e("SearchView", e.getMessage(), e);
        }
    }
}
