package com.example.dialogdemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private Dialog builder = null;
    //列表
	private ListView lv = null;
	private EditText edt = null;
	//添加item
	private Button btn_add = null;
	private Button btn_more = null;
	//删除选中的item
	private Button btn_delete = null;
	//不做任何操作
	private Button btn_cancel = null;

	private LinearLayout more_ll = null;
	private LinearLayout add_ll = null;
	
	private SimpleAdapter adapter  = null;
    
	//记录所有listview的item的数据
	private List<HashMap<String, Object>> mapLists = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/** 显示对话框 **/
	public void showDialog(View view) {
		builder = new Dialog(this);
		builder.setTitle("dialog listview");
		builder.show();

		LayoutInflater inflater = LayoutInflater.from(this);
		View viewDialog = inflater.inflate(R.layout.dialog, null);
		Display display = this.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		//设置对话框的宽高
		LayoutParams layoutParams = new LayoutParams(width * 90 / 100,
				LayoutParams.WRAP_CONTENT);
		builder.setContentView(viewDialog, layoutParams);

		lv = (ListView) viewDialog.findViewById(R.id.lv_remain_item);
		mapLists = new ArrayList<HashMap<String, Object>>();
		// 设置一个默认值
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("name", "ITEM1");
		map.put("checked", false);
		mapLists.add(map);
		adapter = new SimpleAdapter(this, mapLists,
				R.layout.dialog_item, new String[] { "name", "checked" },
				new int[] { R.id.tv_shortcut_text, R.id.cb_shortcut_check });
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				CheckBox cb = (CheckBox) view.findViewById(R.id.cb_shortcut_check);
				HashMap<String,Object> selectMap = mapLists.get(position);
				boolean checked = (Boolean) selectMap.get("checked");
				if(checked){
					cb.setChecked(false);
					selectMap.put("checked", false);
				}else{
					cb.setChecked(true);
					selectMap.put("checked", true);
				}
				
				mapLists.remove(position);
				mapLists.add(position, selectMap);
				//ListView的数据发生变化
				adapter.notifyDataSetChanged();
			}

		});

		btn_add = (Button) viewDialog.findViewById(R.id.btn_add);
		btn_more = (Button) viewDialog.findViewById(R.id.btn_more);
		edt = (EditText) viewDialog.findViewById(R.id.word_et);

		btn_delete = (Button) viewDialog.findViewById(R.id.btn_delete);
		btn_cancel = (Button) viewDialog.findViewById(R.id.btn_cancel);

		more_ll = (LinearLayout) viewDialog.findViewById(R.id.more_ll);
		add_ll = (LinearLayout) viewDialog.findViewById(R.id.add_ll);

		btn_add.setOnClickListener(this);
		btn_more.setOnClickListener(this);
		btn_delete.setOnClickListener(this);
		btn_cancel.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add:
			String text = edt.getText().toString().trim();
			if(text.equals("")){
				Toast.makeText(this, "空数据", Toast.LENGTH_SHORT).show();
			}else{
				edt.setText("");
				HashMap<String,Object> addMap = new HashMap<String,Object>();
				addMap.put("name", text);
				addMap.put("checked", false);
				mapLists.add(addMap);
				adapter.notifyDataSetChanged();
			}
			break;
		case R.id.btn_more:
			more_ll.setVisibility(View.GONE);
			add_ll.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_delete:
			List<HashMap<String,Object>> deleteMap = new ArrayList<HashMap<String,Object>>();
            for(HashMap<String,Object> map:mapLists){
            	if((Boolean)map.get("checked")){
            		deleteMap.add(map);
            	}
            } 
            //移除所有被选中的item
            mapLists.removeAll(deleteMap);
            adapter.notifyDataSetChanged();
			break;
		case R.id.btn_cancel:
            //不做任何操作
			break;
		}
	}
}
