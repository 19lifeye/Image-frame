package com.example.admin.framesimage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.preference.DialogPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;

import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.devsmart.android.ui.HorizontalListView;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.example.admin.framesimage.R.id.Emoji;
import static com.example.admin.framesimage.R.id.img_img;
//import static com.example.admin.framesimage.R.id.undoLy;

public class PhotoEdit extends AppCompatActivity implements View.OnTouchListener,ThumbnailCallback {
    static {
        System.loadLibrary("NativeImageProcessor");
    }
    /////fontitems////
    TextAdapter textadp;
    TextView textView;

    /////////imageviewitem
    private ImageView frameimg, imageView, Emojie;
    Context ctx = this;
    final File myDir = new File(Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/");
    ///listview & recycleview////
    HorizontalListView horizontalListView;
    RecyclerView thumbListView;

    LinearLayout TextLoyout;
    LinearLayout ButtonLayout;
    LinearLayout SelectLy;
    LinearLayout SaveLy;
    LinearLayout seekly;
   RelativeLayout relativeLayout;


    boolean success = false;
    /////////////////Control buttons///////
    ImageButton filter, Rotate, Save, Share, font, Colour, Sticker, Back,Text,TextSize;
     Button Select,Cancle,Undo,Redo;

   //Integer[] emoji = {R.drawable.engry, R.drawable.c, R.drawable.happy, R.drawable.quiet, R.drawable.laufh, R.drawable.nerd};
    Integer[] imgid = {R.drawable.frame1, R.drawable.frame2, R.drawable.frame3, R.drawable.frame4,
            R.drawable.frame5, R.drawable.frame6, R.drawable.frame7, R.drawable.frame8,
            R.drawable.frame9, R.drawable.frame10, R.drawable.frame11, R.drawable.frame12,
            R.drawable.frame13, R.drawable.frame14, R.drawable.frame15};

    private String i;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    // we can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;
    // remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private float oldDist = 1f;
    private float d = 0f;
    private float newRot = 0f;
    private float[] lastEvent = null;
    private Bitmap bmap;

    private static final float MIN_ZOOM = 0.3f;
    private static final float MAX_ZOOM = 2.0f;
    boolean b1 = true;
    int flag=0;
    int flag1=2;


    Toolbar toolbar;
    Context cn;
    Bitmap bmp;
    SeekBar seekBar;
    boolean b2,b3;
    private Activity activity;
    String tx;


    String[] fontres={"fronts/arial.ttf", "fronts/gtw.ttf", "fronts/txt1.ttf", "fronts/txt2.ttf", "fronts/txt3.ttf", "fronts/txt4.ttf", "fronts/vera.ttf", "fronts/ariblk.ttf", "fronts/Ethnocentric.ttf", "fronts/Good Times.ttf", "fronts/vera.ttf"};
    int color = 0xffffff00,x_Delta,y_Delta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_edit);
        activity = this;


        horizontalListView = (HorizontalListView) findViewById(R.id.hlistview);
        thumbListView = (RecyclerView) findViewById(R.id.thumbnails);
        activity = this;

        Undo=(Button)findViewById(R.id.undo);
        Redo=(Button)findViewById(R.id.redo);
        seekly=(LinearLayout)findViewById(R.id.skly);



        textadp=new TextAdapter(PhotoEdit.this,fontres);
       relativeLayout=(RelativeLayout)findViewById(R.id.undoLy);
        TextLoyout=(LinearLayout)findViewById(R.id.txt_layaout);
        ButtonLayout=(LinearLayout)findViewById(R.id.fivebuttonLy);
        TextLoyout.setVisibility(View.INVISIBLE);

       SaveLy=(LinearLayout)findViewById(R.id.saveLy);
       SelectLy=(LinearLayout)findViewById(R.id.selectCancle);
      SelectLy.setVisibility(View.INVISIBLE);
      relativeLayout.setVisibility(View.INVISIBLE);
        seekly.setVisibility(View.INVISIBLE);




        Bundle extras = getIntent().getExtras();
        byte[] byteArray = extras.getByteArray("image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

        seekBar=(SeekBar) findViewById(R.id.seekbar1);
        TextSize=(ImageButton)findViewById(R.id.seekbarIcon);

        textView=(TextView) findViewById(R.id.txt_touch);
        Emojie = (ImageView) findViewById(R.id.Emoji);
        imageView=(ImageView)findViewById(R.id.img_img);
        Text=(ImageButton)findViewById(R.id.textwrite);
        Save = (ImageButton) findViewById(R.id.save);
        Share = (ImageButton) findViewById(R.id.share);
        filter = (ImageButton) findViewById(R.id.filter);
        Rotate = (ImageButton) findViewById(R.id.rotate);
        font = (ImageButton) findViewById(R.id.font);
        Colour = (ImageButton) findViewById(R.id.color);
      //  Sticker = (Button) findViewById(R.id.sticker);
     //   Back = (Button) findViewById(R.id.back);
        Select = (Button) findViewById(R.id.select);
        Cancle = (Button) findViewById(R.id.cancle);

        imageView.setImageBitmap(bmp);
        imageView.setOnTouchListener(this);
       // Emojie.setOnTouchListener(this);


        TextSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                textsize();
                if(b2== true){
                    seekly.setVisibility(View.VISIBLE);
                    b2=false;
                }
                else {
                    seekly.setVisibility(View.INVISIBLE);
                     b2=true;

            }
        }                }




);



        final LinearLayout l_Layout = (LinearLayout) findViewById(R.id.frameLayaut);
        Undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (flag == 2) {
                    textView.setText("");
                    flag = 1;
                } else if (flag == 1) {
                    frameimg.setImageResource(0);
                    flag = 0;
                }

            }

        });

        Redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==2){
                    textView.setText("");
                    flag1=1;
                } else if (flag1 == 1) {
                   frameimg.setImageResource(0);
                    flag1=2;
                }

            }
        });

        //////////////select button/////////

       Select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonLayout.setVisibility(View.VISIBLE);
                TextLoyout.setVisibility(View.INVISIBLE);
                horizontalListView.setVisibility(View.INVISIBLE);
                SelectLy.setVisibility(View.INVISIBLE);
                SaveLy.setVisibility(View.VISIBLE);

            }
        });

        ////////////////text FOnt//////////



///////////////////filter1 Image///////////
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b1==true){
                    filter.setImageResource(R.drawable.filter_2);
                    thumbListView.setVisibility(View.VISIBLE);
                    initHorizontalList();
                    horizontalListView.setVisibility(View.INVISIBLE);
                    b1=false;


                }
                else {
                    filter.setImageResource(R.drawable.fream);
                    Rotate.setImageResource(R.drawable.rotet_2);
                    Save.setImageResource(R.drawable.save_2);
                    Share.setImageResource(R.drawable.share_2);
                    font.setImageResource(R.drawable.text_2);
                    horizontalListView.setVisibility(View.VISIBLE);
                    frame();
                    thumbListView.setVisibility(View.INVISIBLE);
                    b1=true;
                }


            }
        });

        //////////////////////Rotation/////////////////////////
        Rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageView.setRotation(imageView.getRotation() + 90);
                Rotate.setImageResource(R.drawable.rotet);
                Save.setImageResource(R.drawable.save_2);
                Share.setImageResource(R.drawable.share_2);
                font.setImageResource(R.drawable.text_2);
                filter.setImageResource(R.drawable.fream_2);
            }
        });

//////////////Font Colour ////////////////////
        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textStyle();
                TextLoyout.setVisibility(View.VISIBLE);
                ButtonLayout.setVisibility(View.INVISIBLE);
                thumbListView.setVisibility(View.INVISIBLE);
                horizontalListView.setVisibility(View.VISIBLE);
               SelectLy.setVisibility(View.VISIBLE);
               SaveLy.setVisibility(View.INVISIBLE);
                font.setImageResource(R.drawable.text);
                Rotate.setImageResource(R.drawable.rotet_2);
                Save.setImageResource(R.drawable.save_2);
                Share.setImageResource(R.drawable.share_2);
                filter.setImageResource(R.drawable.fream_2);
            }


        });

        Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b3== true){
                    horizontalListView.setAdapter(textadp);
                    horizontalListView.setVisibility(View.VISIBLE);
                    b3=false;
                }
                else {
                    horizontalListView.setVisibility(View.INVISIBLE);
                    b3=true;
                }

                horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Typeface faceee=Typeface.createFromAsset(getAssets(),fontres[i]);
                        textView.setTypeface(faceee);
                    }

                });

            }
        });

        Colour.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false);
            }
        });
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int X = (int) event.getRawX();
                final int Y = (int) event.getRawY();
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        FrameLayout.LayoutParams lParams = (FrameLayout.LayoutParams) v.getLayoutParams();
                        x_Delta= X - lParams.leftMargin;
                        y_Delta = Y - lParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.getLayoutParams();

                        layoutParams.leftMargin = X -  x_Delta;
                        layoutParams.topMargin = Y -  y_Delta;
                        // layoutParams.rightMargin = 250;
                        // layoutParams.bottomMargin = 250;
                        v.setLayoutParams(layoutParams);
                        break;
                }
                textView.invalidate();

                return true;

            }
        });

        //////ActionBar items//////////////////////
     /*  Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }); */

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share.setImageResource(R.drawable.share);
                Save.setImageResource(R.drawable.save_2);
                Rotate.setImageResource(R.drawable.rotet_2);
                font.setImageResource(R.drawable.text_2);
                filter.setImageResource(R.drawable.fream_2);

                l_Layout.setDrawingCacheEnabled(true);
                Bitmap icon = l_Layout.getDrawingCache();
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
                try {
                    file.createNewFile();
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpeg");
                Uri uri = Uri.parse("file:///sdcard/temporary_file.jpg");
                shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                ctx.startActivity(shareIntent);
            }

        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save.setImageResource(R.drawable.save);
                Rotate.setImageResource(R.drawable.rotet_2);
                Share.setImageResource(R.drawable.share_2);
                font.setImageResource(R.drawable.text_2);
                filter.setImageResource(R.drawable.fream_2);

                final Random genertor = new Random();
                int n = 1000;
                n = genertor.nextInt(n);
                final String fname = "imageNm" + n + ".jpg";
                myDir.mkdirs();
                File image = new File(myDir, fname);
                l_Layout.destroyDrawingCache();
                l_Layout.setDrawingCacheEnabled(true);
                Bitmap icon = l_Layout.getDrawingCache();

                // Drawable drawable = ctx.getResources().getDrawable( image_resource[position]);
                //Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

                FileOutputStream outStream;
                try {

                    outStream = new FileOutputStream(image);
                    icon.compress(Bitmap.CompressFormat.JPEG, 100, outStream);


                    outStream.flush();
                    outStream.close();
                    success = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (success) {
                    Toast.makeText(ctx,
                            "Image saved with success at /sdcard/temp_image",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ctx,
                            "Error during image saving", Toast.LENGTH_LONG)
                            .show();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    final Uri contentUri = Uri.fromFile(image);
                    scanIntent.setData(contentUri);
                    ctx.sendBroadcast(scanIntent);
                } else {
                    ctx.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://mnt/sdcard/" + Environment.getExternalStorageDirectory())));
                }


            }
        });
        /* Sticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (b1==true)
                {
                    Emoji();
                    b1=false;

                }
                else
                    {
                        horizontalListView.setVisibility(View.VISIBLE);
                        b1=true;
                        Emoji();


                       // thumbListView.setVisibility(View.INVISIBLE);
                    }

              // Emoji();
              //  thumbListView.setVisibility(View.INVISIBLE);
              //  horizontalListView.setVisibility(View.VISIBLE);
            }
            });   */
        //////////////////////////Tooolbar//////////
        initToolBar();
        }
        public void initToolBar() {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent back=new Intent(PhotoEdit.this,MainActivity.class);
            startActivity(back);
        }

        return super.onOptionsItemSelected(item);
    }

    public void openDialog(boolean b)
    {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(PhotoEdit.this, color, b, new AmbilWarnaDialog.OnAmbilWarnaListener()
        {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                PhotoEdit.this.color = color;
                textView.setTextColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getApplicationContext(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }
    private void textStyle() {
        textView = (TextView) findViewById(R.id.txt_touch);
        final AlertDialog onea = new AlertDialog.Builder(PhotoEdit.this).create();
        onea.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        final EditText input = new EditText(PhotoEdit.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        onea.setView(input);

       // tx=input.getText().toString().trim();


        onea.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                if(tx.equals(""))
//                {
//                    input.setError("write text");
//
//                }
//                else {
                    textView.setText(input.getText().toString());
                    flag = 2;
                    relativeLayout.setVisibility(View.VISIBLE);
//                }







               /*horizontalListView.setAdapter(textadp);
                horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Typeface faceee=Typeface.createFromAsset(getAssets(),fontres[i]);
                        textView.setTypeface(faceee);
                    }

                }); */

            }


        });

        onea.setButton(Dialog.BUTTON_NEGATIVE, "cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onea.dismiss();
                ButtonLayout.setVisibility(View.VISIBLE);
                TextLoyout.setVisibility(View.INVISIBLE);
                SaveLy.setVisibility(View.INVISIBLE);
                SelectLy.setVisibility(View.INVISIBLE);


            }
        });
        onea.show();

    }
    public  void textsize(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
 /////////////////////emoji//////////////
   /* public void Emoji() {
        Emojiadapter adater = new Emojiadapter(PhotoEdit.this, emoji);
        horizontalListView.setAdapter(adater);
        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pos = Integer.toString(position);


                Emojie = (ImageView) findViewById(R.id.Emoji);
                Emojie.setImageResource(emoji[position]);
                if(Emojie!= null){
                    flag=3;

                }


            }
        });
    } */
////////////////////////frame//////////
    public void frame() {
        Adapter adapter = new Adapter(PhotoEdit.this, imgid);

        horizontalListView.setAdapter(adapter);

        horizontalListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                String pos = Integer.toString(position);


               relativeLayout.setVisibility(View.VISIBLE);

                frameimg = (ImageView) findViewById(R.id.img_frame);
                frameimg.setScaleType(ImageView.ScaleType.FIT_XY);
                frameimg.setImageResource(imgid[position]);
                if(frameimg.getDrawable()!= null){
                    flag = 1;
                }

                Toast.makeText(PhotoEdit.this, "Your Position is" + pos, Toast.LENGTH_SHORT).show();

            }
        });

    }




    private void initHorizontalList() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.scrollToPosition(0);
        thumbListView.setLayoutManager(layoutManager);
        thumbListView.setHasFixedSize(true);
        bindDataToAdapter();
    }


    private void bindDataToAdapter() {
        final Context context = this.getApplication();
        Handler handler = new Handler();
        Runnable r = new Runnable() {
            public void run() {

                imageView.setDrawingCacheEnabled(true);
                Bitmap thumbImage = imageView.getDrawingCache();
                //Bitmap thumbImage = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.msd), 640, 640, false);
                ThumbnailItem t1 = new ThumbnailItem();
                ThumbnailItem t2 = new ThumbnailItem();
                ThumbnailItem t3 = new ThumbnailItem();
                ThumbnailItem t4 = new ThumbnailItem();
                ThumbnailItem t5 = new ThumbnailItem();
                ThumbnailItem t6 = new ThumbnailItem();

                t1.image = thumbImage;
                t2.image = thumbImage;
                t3.image = thumbImage;
                t4.image = thumbImage;
                t5.image = thumbImage;
                t6.image = thumbImage;
                ThumbnailsManager.clearThumbs();
                ThumbnailsManager.addThumb(t1); // Original Image

                t2.filter = SampleFilters.getStarLitFilter();
                ThumbnailsManager.addThumb(t2);

                t3.filter = SampleFilters.getBlueMessFilter();
                ThumbnailsManager.addThumb(t3);

                t4.filter = SampleFilters.getAweStruckVibeFilter();
                ThumbnailsManager.addThumb(t4);

                t5.filter = SampleFilters.getLimeStutterFilter();
                ThumbnailsManager.addThumb(t5);

                t6.filter = SampleFilters.getNightWhisperFilter();
                ThumbnailsManager.addThumb(t6);

                List<ThumbnailItem> thumbs = ThumbnailsManager.processThumbs(context);

                ThumbnailsAdapter adapter = new ThumbnailsAdapter(thumbs, (ThumbnailCallback) activity);
                thumbListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        };

        handler.post(r);
    }

////////////zoooooooom IN aur Out////////////////////////

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        imageView = (ImageView) v;
      //  Emojie = (ImageView) v;
        //Emojie.setScaleType(ImageView.ScaleType.MATRIX);
        imageView.setScaleType(ImageView.ScaleType.MATRIX);
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                mode = DRAG;
                lastEvent = null;
                Log.e("Tuoch Touch", "Drag");






                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;

                }
                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                Log.e("Tuoch Touch", "Rotate");
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                lastEvent = null;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG) {
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;


                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float newDist = spacing(event);

                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        float scale = (newDist / oldDist);


                        matrix.postScale(scale, scale, mid.x, mid.y);
                        Log.e("Tuoch Touch", "sss");
                    }
                    if (lastEvent != null && event.getPointerCount() == 2 || event.getPointerCount() == 3) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        float[] values = new float[9];
                        matrix.getValues(values);
                        float tx = values[2];
                        float ty = values[2];
                        float sx = values[0];

                      /*  matrix.getValues(values);

                        Float etx = values[2];
                        Float ety = values[5];
                        Float esx = values[0]; */

                        //set Min and Max zoom
                        float scaleX = values[Matrix.MSCALE_X];
                        float scaleY = values[Matrix.MSCALE_Y];
                        if (scaleX > MAX_ZOOM) {
                            scaleX = MAX_ZOOM;
                        } else if (scaleX < MIN_ZOOM) {
                            scaleX = MIN_ZOOM;
                        }

                        if (scaleY > MAX_ZOOM) {
                            scaleY = MAX_ZOOM;
                        } else if (scaleY < MIN_ZOOM) {
                            scaleY = MIN_ZOOM;
                        }
                        float xc = (imageView.getWidth() / 2) * sx;
                        float yc = (imageView.getHeight() / 2) * sx;
                       // float exc = (Emojie.getWidth() / 2) * esx;
                       // float eyc = (Emojie.getHeight() / 2) * esx;
                        matrix.postRotate(r, tx + xc, ty + yc);
                    }
                }
                break;
        }

        imageView.setImageMatrix(matrix);
        //  Emojie.setImageMatrix(matrix);


        bmap = Bitmap.createBitmap(imageView.getWidth(), imageView.getHeight(), Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bmap);
        imageView.draw(canvas);

       // bmap = Bitmap.createBitmap(Emojie.getWidth(), Emojie.getHeight(), Bitmap.Config.RGB_565);
      //  Canvas canvas1 = new Canvas(bmap);
      //  Emojie.draw(canvas1);


        //fin.setImageBitmap(bmap);
        return true;
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        float s = x * x + y * y;
        return (float) Math.sqrt(s);
    }


    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /**
     * Calculate the degree to be rotated by.
     *
     * @param event
     * @return Degrees
     */
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(0));
        double delta_y = (event.getY(0) - event.getY(0));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }


    @Override
    public void onThumbnailClick(Filter filter) {
        imageView.setImageBitmap(bmp);
        imageView.destroyDrawingCache();
       // imageView.setDrawingCacheEnabled(true);
        imageView.setImageBitmap(filter.processFilter(imageView.getDrawingCache()));


    }

}

////////////////////////////////////////////////////////
/*  Zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                // set decreased value of scale x and y to
                imageView.setScaleX((float) (x + .4));
                imageView.setScaleY((float) (y + .4));ZXXXXXXXXa


            }
        });
        Remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                float x = imageView.getScaleX();
                float y = imageView.getScaleY();
                // set decreased value of scale x and y to
                imageView.setScaleX((float) (x - .4));
                imageView.setScaleY((float) (y - .4));

            }
        });*/
//   final TouchImageView touch = (TouchImageView)findViewById(R.id.YOUR_TOUCH_IMAGE_VIEW_)ID);

//    Bitmap bImage = BitmapFactory.decodeResource(this.getResources(), R.drawable.DRAWABLE_ID);

//  touch.setImageBitmap(bImage);

//   touch.setMaxZoom(4f); //change the max level of zoom, default is 3f




/* if(b1==true)
         {
         frame();
         b1=false;
         }
         else {
         b1=true;
         }
         */
 /*
                final CharSequence[] items = { "Take Photo", "Choose from Library",
                        "Cancel" };
                AlertDialog.Builder builder=new AlertDialog.Builder(EditImageActivity.this);
                builder.setTitle("Add Photo");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {


                        if (items[item].equals("Take Photo")) {
                            userChoosenTask ="Take Photo";


                        } else if (items[item].equals("Choose from Library")) {
                            userChoosenTask ="Choose from Library";

                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();*/

// openDialog(false);









