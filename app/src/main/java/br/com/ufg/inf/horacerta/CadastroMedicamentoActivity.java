package br.com.ufg.inf.horacerta;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dao.MedicamentoDAO;
import model.Medicamento;
import model.Usuario;

public class CadastroMedicamentoActivity extends AppCompatActivity{

    TextView btnHrInicio;
    TextView btnDtInicio;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_medicamento);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 0);
            }
        });

        btnDtInicio = (TextView) findViewById(R.id.btnDtInicio);
        btnDtInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });

        btnHrInicio = (TextView) findViewById(R.id.btnHrInicio);
        btnHrInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_medicamento, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salvar) {
            Toast toast;
            ImageView medicamentoImagem = (ImageView) findViewById(R.id.medicamentoImagem);
            EditText nomeMedicamento = (EditText) findViewById(R.id.medicamentoNome);
            EditText medicamentoDescricaoUso = (EditText) findViewById(R.id.medicamentoDescricaoUso);
            EditText intervalo = (EditText) findViewById(R.id.intervalo);
            TextView btnDtInicio = (TextView) findViewById(R.id.btnDtInicio);
            TextView btnHrInicio = (TextView) findViewById(R.id.btnHrInicio);

            if("".equals(nomeMedicamento.getText().toString().trim())){
                toast = Toast.makeText(getApplicationContext(), "Informe o nome do medicamento", Toast.LENGTH_LONG);
                toast.show();

            }else if("".equals(intervalo.getText().toString().trim())) {
                toast = Toast.makeText(getApplicationContext(), "Informe o intervalo do usao do medicamento", Toast.LENGTH_LONG);
                toast.show();

            }else if ("Data".equals(btnDtInicio.getText().toString().trim()) || "Hora".equals(btnHrInicio.getText().toString().trim())){
                toast = Toast.makeText(getApplicationContext(), "Informe a data e hora do início do uso do medicamento", Toast.LENGTH_LONG);
                toast.show();

            }else {
                Medicamento novoMedicamento = new Medicamento();
                novoMedicamento.setUsuario(Usuario.getUsuarioInstance());

                try {
                    medicamentoImagem.setDrawingCacheEnabled(true);
                    medicamentoImagem.buildDrawingCache();
                    Bitmap imageMedicamentoBM = medicamentoImagem.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    imageMedicamentoBM.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    novoMedicamento.setImagem(stream.toByteArray());

                }catch(Exception e){
                    e.printStackTrace();
                }

                novoMedicamento.setNome(nomeMedicamento.getText().toString().trim());
                novoMedicamento.setDescricaoDoUso(medicamentoDescricaoUso.getText().toString().trim());
                novoMedicamento.setIntervaloEmMinutos(Integer.parseInt(intervalo.getText().toString().trim()));

                String dataInicioUsoMedicamento = btnDtInicio.getText().toString().trim()+" "+btnHrInicio.getText().toString().trim();
                String pattern = "dd/MM/yyyy HH:mm";
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                try {
                    novoMedicamento.setDtInicio(format.parse(dataInicioUsoMedicamento));
                } catch (ParseException e) {
                    novoMedicamento.setDtInicio(new Date());
                    e.printStackTrace();
                }

                MedicamentoDAO medicamentoDAO = new MedicamentoDAO(getApplicationContext());
                medicamentoDAO.insert(novoMedicamento);
                toast = Toast.makeText(getApplicationContext(), "Medicamento salvo com sucesso", Toast.LENGTH_LONG);
                toast.show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            }
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 0 && resultCode == RESULT_OK ) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            ImageView imagemMedicamento = (ImageView)findViewById(R.id.medicamentoImagem);
            imagemMedicamento.getLayoutParams().width = (int) getApplicationContext().getResources().getDimension(R.dimen.imageview_width);
            imagemMedicamento.getLayoutParams().height = (int) getApplicationContext().getResources().getDimension(R.dimen.imageview_height);
            imagemMedicamento.setImageBitmap(bp);
        }
    }

    public void showDatePickerDialog(View v) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDateTimePicker;
        mDateTimePicker = new DatePickerDialog(CadastroMedicamentoActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnDtInicio.setText(day+"/"+(month+1)+"/"+year);
            }
        },  year, month, day);
        mDateTimePicker.setTitle("Data de início");
        mDateTimePicker.show();
    }

    public void showTimePickerDialog(View v) {

        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CadastroMedicamentoActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                btnHrInicio.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Hora início");
        mTimePicker.show();

    }


}
