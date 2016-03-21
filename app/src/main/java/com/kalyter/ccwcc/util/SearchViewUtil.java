package com.kalyter.ccwcc.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kalyter.ccwcc.R;
import com.kalyter.ccwcc.data.CCWCCSQLiteHelper;


public class SearchViewUtil {

    private Context mContext;
    private SimpleCursorAdapter mSimpleCursorAdapter;
    private TextView mTextView;
    private SearchView mSearchView;
    private CCWCCSQLiteHelper dbHelper;
    private JSONArray data;
    private RecordListAdapter adapter;

    public SearchViewUtil(final SearchView mSearchView, TextView mTextView) {
        this.mSearchView=mSearchView;
        this.mTextView=mTextView;
        init();
    }

    private void init() {
        this.mContext=mSearchView.getContext();
        dbHelper = new CCWCCSQLiteHelper(mContext);
        Cursor birdAllNameCursor = dbHelper.getBirdAllNamesCursor(null);
        mSimpleCursorAdapter=new SimpleCursorAdapter(mContext,android.R.layout.simple_list_item_1, birdAllNameCursor,new String[]{"namezh"}, new int[]{android.R.id.text1});
        mSearchView.setSuggestionsAdapter(mSimpleCursorAdapter);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor cursor = dbHelper.getBirdAllNamesCursor(newText);
                mSimpleCursorAdapter = new SimpleCursorAdapter(mContext
                        , android.R.layout.simple_list_item_1, cursor
                        , new String[]{"namezh"}
                        , new int[]{android.R.id.text1});
                mSearchView.setSuggestionsAdapter(mSimpleCursorAdapter);
                return false;
            }
        });
    }

    public SearchViewUtil(SearchView mSearchView, RecordListAdapter adapter) {
        this.mSearchView = mSearchView;
        this.adapter=adapter;
        this.data=adapter.getData();
        init();
    }

    public void setDefaultAdapter(){
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                Cursor cursor = mSearchView.getSuggestionsAdapter().getCursor();
                cursor.moveToFirst();
                int i = 0;
                while (i < position) {
                    cursor.moveToNext();
                    i++;
                }
                String name = cursor.getString(1);
                mTextView.setText(name);
                return false;
            }
        });
    }


    public void setInputQuantityAdapter(){
        mSearchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                return false;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                final JSONObject t = new JSONObject();
                Cursor cursor = mSearchView.getSuggestionsAdapter().getCursor();
                cursor.moveToFirst();
                int i = 0;
                while (i < position) {
                    cursor.moveToNext();
                    i++;
                }
                String name = cursor.getString(1);
                t.put(RecordUtil.BIRD_NAME_KEY, name);
                final EditText editText = new EditText(mContext);
                editText.setHint(R.string.hint_add_quantity);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);

                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle(name)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(editText)
                        .setPositiveButton(mContext.getResources().getString(R.string.dialog_positive_button_caption_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!editText.getText().equals("")) {
                                    t.put(RecordUtil.QUANTITY_KEY, Integer.valueOf(editText.getText().toString()));
                                    data.add(t);
                                    adapter.notifyDataSetChanged();
                                    InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                                }
                            }
                        }).setNegativeButton(R.string.dialog_positive_button_caption_cancel, null)
                        .setCancelable(false)
                        .create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                });

                dialog.show();
                mSearchView.setQuery("", false);
                mSearchView.clearFocus();
                return false;
            }
        });
    }

}
