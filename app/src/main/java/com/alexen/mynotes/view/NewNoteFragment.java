package com.alexen.mynotes.view;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alexen.mynotes.R;
import com.alexen.mynotes.model.Note;
import com.alexen.mynotes.viewmodel.MyNotesViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewNoteFragment extends Fragment {
    private MyNotesViewModel myNotesViewModel;
    private NavController navController;

    private EditText titleEditText, contentEditText;
//    private Button addTarea;

    private Note noteSelecionada;

    public NewNoteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        navController = Navigation.findNavController(view);
        myNotesViewModel= ViewModelProviders.of(requireActivity()).get(MyNotesViewModel.class);


        titleEditText = view.findViewById(R.id.editTextTitle);
        contentEditText = view.findViewById(R.id.editTextContent);

        if (myNotesViewModel.isUserEdit) {
            noteSelecionada = myNotesViewModel.obtenerNotasDetallePorId(myNotesViewModel.userToEditId);
            String[] sep = noteSelecionada.getFullContent().split("/");

            titleEditText.setText(sep[0]);
            contentEditText.setText(sep[1]);
        }


        view.findViewById(R.id.fab_save_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contentEditText.getText().toString().isEmpty()){
                    contentEditText.setError("Introduzca el contenido");
                    return;
                }
                if(titleEditText.getText().toString().isEmpty()){
                    titleEditText.setError("Introduzca el titulo");
                    return;
                }
                if (myNotesViewModel.isUserEdit) {
                    Note note = new Note();
                    note.setFullContent(titleEditText.getText().toString()+"/"+contentEditText.getText().toString());

                    myNotesViewModel.actualizarNota(myNotesViewModel.userToEditId, note);
                    noteSelecionada = null;
                    myNotesViewModel.isUserEdit = false;
                    myNotesViewModel.userToEditId = 0;
                }
                else myNotesViewModel.insertarNota(new Note(titleEditText.getText().toString()+"/"+ contentEditText.getText().toString()));
                navController.popBackStack();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.borrar_nota:
                myNotesViewModel.eliminarNota(noteSelecionada.getId());

                Toast.makeText(getContext(),"Nota borrada",Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.nav_home);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
        // You can look up you menu item here and store it in a global variable by
        // 'mMenuItem = menu.findItem(R.id.my_menu_item);'
    }

}
