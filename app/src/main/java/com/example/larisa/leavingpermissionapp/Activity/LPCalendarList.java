package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapter;
import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterLP;
import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LPCalendarList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecycleViewAdapterLP recycleViewAdapterLP;
    private List<LP> lpList;
    private Button backToCalendar;
    private Button doneConfirming;
    private TextView listDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpcalendar_list);

        listDate = findViewById(R.id.lpListDate);
        recyclerView = findViewById(R.id.recycleViewLP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lpList = new ArrayList<>();
        Intent intent = getIntent();
        List<LP> lpList = (List<LP>) intent.getSerializableExtra("TodayLP");


        if (lpList != null) {
            listDate.setText(lpList.get(0).getData());
        }

        recycleViewAdapterLP = new RecycleViewAdapterLP(LPCalendarList.this, lpList);

        recyclerView.setAdapter(recycleViewAdapterLP);
        recycleViewAdapterLP.notifyDataSetChanged();
        backToCalendar = findViewById(R.id.backToCalendar);
        doneConfirming = findViewById(R.id.finishedReviewing);


        backToCalendar.setOnClickListener(this);
        doneConfirming.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finishedReviewing:
//
                final Boolean[] found = new Boolean[1];

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                for (final String key : recycleViewAdapterLP.modifiedLP.keySet()) {
                    found[0] = false;
                    final LP lp = recycleViewAdapterLP.modifiedLP.get(key);

                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                search:
                                {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.child("fullName").getValue(String.class).equals(lp.getNume())) {
                                            for (DataSnapshot snapshot1 : snapshot.child("LP").getChildren()) {
                                                if (snapshot1.getKey().equals(lp.getData())) {
                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
//
                                                        if (snapshot2.child("id").getValue().equals(key)) {
                                                            if (lp.getStatus().equals("confirmat")) {


                                                                AssetManager assetManager = getAssets();
                                                                InputStream myInput;


                                                                try {

                                                                    myInput = assetManager.open("abc.xls");
                                                                    POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

                                                                    HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

                                                                    HSSFSheet mySheet = myWorkBook.getSheetAt(0);

                                                                    String fullNume[] = lp.getUser().getFullName().split(" ");

                                                                    HSSFCell cell;
                                                                    //Nume
                                                                    cell = mySheet.getRow(6).getCell(2);
                                                                    cell.setCellValue(fullNume[1].toUpperCase());
                                                                    //Prenume
                                                                    cell = mySheet.getRow(6).getCell(6);
                                                                    cell.setCellValue(fullNume[0]);


                                                                    //Matricol
                                                                    cell = mySheet.getRow(8).getCell(2);
                                                                    cell.setCellValue(lp.getUser().getNrMatricol());
                                                                    //Absent de la
                                                                    cell = mySheet.getRow(8).getCell(5);
                                                                    cell.setCellValue(lp.getData());
                                                                    //Absent pana la
                                                                    cell = mySheet.getRow(8).getCell(7);
                                                                    cell.setCellValue(lp.getData());
                                                                    //De la ora
                                                                    cell = mySheet.getRow(12).getCell(5);
                                                                    cell.setCellValue(lp.getFrom());
                                                                    //Pana la ora
                                                                    cell = mySheet.getRow(12).getCell(7);
                                                                    cell.setCellValue(lp.getTo());
                                                                    //Data depunere
                                                                    cell = mySheet.getRow(18).getCell(3);
                                                                    cell.setCellValue(lp.getData());
                                                                    //Data confirmare
                                                                    cell = mySheet.getRow(18).getCell(8);
                                                                    cell.setCellValue(lp.getData());
                                                                    //            //Adresa si numar de telefon
                                                                    cell = mySheet.getRow(21).getCell(1);
                                                                    cell.setCellValue(lp.getUser().getTelefon());


                                                                    final InputStream stream =
                                                                            LPCalendarList.this.getAssets().open(fullNume[0] + fullNume[1] + ".png");
                                                                    byte[] imageBytes = IOUtils.toByteArray(stream);
                                                                    final int pictureIndex =
                                                                            myWorkBook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
                                                                    stream.close();
                                                                    final CreationHelper helper = myWorkBook.getCreationHelper();
                                                                    final Drawing drawing = mySheet.createDrawingPatriarch();

                                                                    final ClientAnchor anchor = helper.createClientAnchor();
                                                                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);


                                                                    anchor.setCol1(3);
                                                                    anchor.setCol2(5);
                                                                    anchor.setRow1(15); // same row is okay
                                                                    anchor.setRow2(18);

                                                                    final Picture pict = drawing.createPicture(anchor, pictureIndex);


                                                                    File path = LPCalendarList.this.getFilesDir();

                                                                    FileOutputStream outFile =
                                                                            new FileOutputStream(new File(path, "/" +
                                                                                    "Cerere_Absenta_" + fullNume[1].toUpperCase() + "_" + fullNume[0] + "_"
                                                                                    + lp.getData() + "_" + lp.getFrom() + "_991" +
                                                                                    ".xls"));
                                                                    myWorkBook.write(outFile);
                                                                    outFile.close();

                                                                } catch (IOException e) {
                                                                    e.printStackTrace();
                                                                }
                                                                snapshot2.child("status").getRef().setValue("confirmat");
//                                                        recycleViewAdapterLP.notifyItemChanged(i);
                                                                found[0] = true;

//                                                           break search;


                                                            } else {
                                                                snapshot2.child("status").getRef().setValue("");
                                                                snapshot2.child("status").getRef().setValue("refuzat");

                                                                found[0] = true;

                                                            }


                                                        }
                                                    }
                                                }


                                            }


                                        }


                                    }

                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                finish();


                break;
            case R.id.backToCalendar:
                finish();
                break;


        }
    }

}
