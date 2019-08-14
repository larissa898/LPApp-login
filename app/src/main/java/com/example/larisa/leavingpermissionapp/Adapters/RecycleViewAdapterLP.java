package com.example.larisa.leavingpermissionapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.HardwarePropertiesManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Activity.LPCalendarList;
import com.example.larisa.leavingpermissionapp.Model.LP;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecycleViewAdapterLP extends RecyclerView.Adapter <RecycleViewAdapterLP.ViewHolder> {

    private Context context;
    private List<LP> lps = new ArrayList<>();
    public HashMap<String,LP> modifiedLP = new HashMap<>();


        public RecycleViewAdapterLP(Context context, List<LP> lps) {
        this.context = context;
        this.lps = lps;
    }

    @NonNull
    @Override
    public RecycleViewAdapterLP.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lp_row, parent, false);
        return new RecycleViewAdapterLP.ViewHolder(v, context);

    }


    @Override
    public void onBindViewHolder(RecycleViewAdapterLP.ViewHolder holder, final int position) {

        LP lp = lps.get(position);
        holder.numeAngajat.setText(lp.getNume());
        holder.fromTime.setText(lp.getFrom());
        holder.toTime.setText(lp.getTo());
        holder.totalHours.setText(lp.getTotal().toString());
        holder.status.setText(lp.getStatus());
        holder.setItemClickListener((new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {


                modifiedLP.put(lps.get(position).getId(),lps.get(position));
            }
        }));



    }


    @Override
    public int getItemCount() {
        return lps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView numeAngajatLabel;
        public TextView fromTimeLabel;
        public TextView toTimeLabel;
        public TextView totalHoursLabel;
        public TextView numeAngajat;
        public TextView fromTime;
        public TextView toTime;
        public TextView totalHours;
        public Button acceptButton;
        public Button refuseButton;
        public TextView statusLabel;
        public TextView status;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            numeAngajatLabel = v.findViewById(R.id.numeAngajat);
            fromTimeLabel = v.findViewById(R.id.fromLP);
            toTimeLabel = v.findViewById(R.id.toLP);
            totalHoursLabel = v.findViewById(R.id.totalOre);
            numeAngajat = v.findViewById(R.id.textViewNumeEdit);
            fromTime = v.findViewById(R.id.textViewFromLP);
            toTime = v.findViewById(R.id.textViewToLP);
            totalHours = v.findViewById(R.id.textViewTotal);
            acceptButton = v.findViewById(R.id.acceptButton);
            refuseButton = v.findViewById(R.id.refuseButton);
            statusLabel = v.findViewById(R.id.statusLP);
            status = v.findViewById(R.id.textViewStatus);


            acceptButton.setOnClickListener(this);
            refuseButton.setOnClickListener(this);


        }


        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptButton:
                    int position = getAdapterPosition();
                    LP LivingPerm = lps.get(position);
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Confirming this will generate an excel file for the LP. Do you want to " +
                            "proceed?");
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                        }
                    });
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                            intent.setType("application/excel");
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.putExtra("file", (Serializable) createExcel(LivingPerm));
                            context.startActivity(intent);


                            acceptLP(LivingPerm, position);

                        }
                    });
                    builder.create();
                    builder.show();
                    this.itemClickListener.onItemClick(v, getLayoutPosition());


                    break;
                case R.id.refuseButton:
                    position = getAdapterPosition();
                     LP LivingPerm2 = lps.get(position);
//                    if(LivingPerm.getStatus().equals("neconfirmat")){
                    refuseLP(LivingPerm2);
                    this.itemClickListener.onItemClick(v, getLayoutPosition());



//                    }
                    break;


            }


        }

        public void acceptLP(final LP LivingPerm, final int position) {
            LivingPerm.setStatus("confirmat");
            notifyItemChanged(position);
        }

        public void refuseLP(final LP LivingPerm) {
            LivingPerm.setStatus("refuzat");
            notifyDataSetChanged();

        }
        public InputStream createExcel(LP lp)
        {
            AssetManager assetManager = context.getAssets();
            InputStream myInput = null;


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
                        context.getAssets().open(fullNume[0] + fullNume[1] + ".png");
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


                File path = context.getFilesDir();

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
            return myInput;
        }


    }
}


