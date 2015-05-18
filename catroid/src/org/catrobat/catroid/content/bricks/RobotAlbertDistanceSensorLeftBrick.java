/**
 *  Catroid: An on-device visual programming system for Android devices
 *  Copyright (C) 2010-2015 The Catrobat Team
 *  (<http://developer.catrobat.org/credits>)
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as
 *  published by the Free Software Foundation, either version 3 of the
 *  License, or (at your option) any later version.
 *  
 *  An additional term exception under section 7 of the GNU Affero
 *  General Public License, version 3, is available at
 *  http://developer.catrobat.org/license_additional_term
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Affero General Public License for more details.
 *  
 *  You should have received a copy of the GNU Affero General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.content.bricks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.actions.ExtendedActions;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.adapter.DataAdapter;
import org.catrobat.catroid.ui.adapter.UserVariableAdapterWrapper;
import org.catrobat.catroid.ui.dialogs.NewDataDialog;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class RobotAlbertDistanceSensorLeftBrick extends FormulaBrick implements OnClickListener {
	private static final long serialVersionUID = 1L;
	private UserVariable userVariable;
	private transient AdapterView<?> adapterView;
	public boolean inUserBrick = false;

	public RobotAlbertDistanceSensorLeftBrick( Formula variableFormula, UserVariable userVariable,boolean inUserBrick) {
		this.userVariable = userVariable;
		this.inUserBrick = inUserBrick;
		initializeBrickFields(variableFormula);
	}

	private void initializeBrickFields(Formula brightness) {
		addAllowedBrickField(BrickField.ROBOT_ALBERT_DISTANCE_SENSOR_LEFT);
		setFormulaWithBrickField(BrickField.ROBOT_ALBERT_DISTANCE_SENSOR_LEFT, brightness);
	}
	@Override
	public int getRequiredResources() {
		return BLUETOOTH_ROBOT_ALBERT;
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite,SequenceAction sequence) {
		sequence.addAction(ExtendedActions.robotAlbertDistanceSensorLeft(sprite,  userVariable));
		return null;
	}

	@Override
	public View getView(final Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}
		if (view == null) {
			alphaValue = 255;
		}

		view = View.inflate(context, R.layout.brick_robot_albert_distance_sensor_left, null);
		view = getViewWithAlpha(alphaValue);
		setCheckboxView(R.id.brick_robot_albert_distance_sensor_left_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		View prototypeView = View.inflate(context, R.layout.brick_robot_albert_distance_sensor_left, null);
		Spinner variableSpinner = (Spinner) prototypeView.findViewById(R.id.robot_albert_distance_sensor_left_spinner);
		variableSpinner.setFocusableInTouchMode(false);
		variableSpinner.setFocusable(false);
		UserBrick currentBrick = ProjectManager.getInstance().getCurrentUserBrick();
		int userBrickId = (currentBrick == null ? -1 : currentBrick.getDefinitionBrick().getUserBrickId());
		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentProject().getDataContainer().
				createDataAdapter(context, userBrickId, ProjectManager.getInstance().getCurrentSprite(), inUserBrick);

		UserVariableAdapterWrapper userVariableAdapterWrapper = new UserVariableAdapterWrapper(context,
				dataAdapter);
		userVariableAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);
		variableSpinner.setAdapter(userVariableAdapterWrapper);
		setSpinnerSelection(variableSpinner, null);

		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.brick_robot_albert_distance_sensor_left_layout);
		Drawable background = layout.getBackground();
		background.setAlpha(alphaValue);

		TextView textSetVariable = (TextView) view.findViewById(R.id.brick_robot_albert_distance_sensor_left_label);
		Spinner variableBrickSpinner = (Spinner) view.findViewById(R.id.robot_albert_distance_sensor_left_spinner);

		ColorStateList color = textSetVariable.getTextColors().withAlpha(alphaValue);
		variableBrickSpinner.getBackground().setAlpha(alphaValue);
		if (adapterView != null) {
			((TextView) adapterView.getChildAt(0)).setTextColor(color);
		}
		textSetVariable.setTextColor(textSetVariable.getTextColors().withAlpha(alphaValue));

		this.alphaValue = (alphaValue);
		return view;
	}



	@Override
	public void onClick(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view,this,BrickField.ROBOT_ALBERT_DISTANCE_SENSOR_LEFT);
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {

	}


	private void updateUserVariableIfDeleted(UserVariableAdapterWrapper userVariableAdapterWrapper) {
		if (userVariable != null && userVariableAdapterWrapper.getPositionOfItem(userVariable) == 0) {
			userVariable = null;
		}
	}

	private void setSpinnerSelection(Spinner variableSpinner, UserVariable newUserVariable) {
		UserVariableAdapterWrapper userVariableAdapterWrapper = (UserVariableAdapterWrapper) variableSpinner
				.getAdapter();

		updateUserVariableIfDeleted(userVariableAdapterWrapper);

		if (userVariable != null) {
			variableSpinner.setSelection(userVariableAdapterWrapper.getPositionOfItem(userVariable), true);
		} else if (newUserVariable != null) {
			variableSpinner.setSelection(userVariableAdapterWrapper.getPositionOfItem(newUserVariable), true);
			userVariable = newUserVariable;
		} else {
			variableSpinner.setSelection(userVariableAdapterWrapper.getCount() - 1, true);
			userVariable = userVariableAdapterWrapper.getItem(userVariableAdapterWrapper.getCount() - 1);
		}
	}
}
