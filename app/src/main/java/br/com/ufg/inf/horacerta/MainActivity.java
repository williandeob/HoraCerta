package br.com.ufg.inf.horacerta;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import dao.MedicamentoDAO;
import model.Medicamento;
import model.Usuario;
import schema.Database;
import service.AlarmService;
import util.UtilService;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!UtilService.isServiceRunning(AlarmService.class.getName(),getApplicationContext())){
            startService(new Intent(this, AlarmService.class));
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = null;
            if(getArguments().getInt(ARG_SECTION_NUMBER) == 0) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);

                MedicamentoDAO medicamentoDAO = new MedicamentoDAO(getContext());
                List<Medicamento>listaDeMedicamentos = medicamentoDAO.findAll();

                ListView lv = (ListView)rootView.findViewById(R.id.listaDeMedicamentos);
                lv.setAdapter(new AdapterListViewMedicamentos(getContext(), listaDeMedicamentos));

                FloatingActionButton fabNovoMedicamento = (FloatingActionButton) rootView.findViewById(R.id.fabNovoMedicamento);
                fabNovoMedicamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), CadastroMedicamentoActivity.class);
                        startActivity(intent);
                    }
                });

            }else{
                rootView = inflater.inflate(R.layout.fragment_user, container, false);
                TextView nameUser = (TextView) rootView.findViewById(R.id.name_user);
                nameUser.setText(Usuario.getUsuarioInstance().getNome());
                TextView emailUser = (TextView) rootView.findViewById(R.id.email_user);
                emailUser.setText(Usuario.getUsuarioInstance().getEmail());

                FloatingActionButton fabNovoMedicamento = (FloatingActionButton) rootView.findViewById(R.id.logout);
                fabNovoMedicamento.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Database schema = new Database(getContext());
                        SQLiteDatabase db = schema.getWritableDatabase();

                        try {
                            db.delete("MEDICAMENTO", null, null);
                            db.delete("USUARIO", null, null);
                            Usuario.cleanInstance();

                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }finally {
                            db.close();
                        }
                    }
                });
            }

            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Medicamentos";
                case 1:
                    return "Perfil";
            }
            return null;
        }
    }
}
