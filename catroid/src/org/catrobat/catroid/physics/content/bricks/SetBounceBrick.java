/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2016 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.physics.content.bricks;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.FormulaBrick;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;

import java.util.List;

public class SetBounceBrick extends FormulaBrick {
	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public SetBounceBrick() {
		addAllowedBrickField(BrickField.PHYSICS_BOUNCE_FACTOR);
	}

	public SetBounceBrick(float bounceFactor) {
		initializeBrickFields(new Formula(bounceFactor));
	}

	public SetBounceBrick(Formula bounceFactor) {
		initializeBrickFields(bounceFactor);
	}

	private void initializeBrickFields(Formula bounceFactor) {
		addAllowedBrickField(BrickField.PHYSICS_BOUNCE_FACTOR);
		setFormulaWithBrickField(BrickField.PHYSICS_BOUNCE_FACTOR, bounceFactor);
	}

	@Override
	public int getRequiredResources() {
		return PHYSICS;
	}

	@Override
	public View getView(Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, R.layout.brick_physics_set_bounce_factor, null);
		view = getViewWithAlpha(alphaValue);

		setCheckboxView(R.id.brick_set_bounce_factor_checkbox);

		final Brick brickInstance = this;
		checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				checked = isChecked;
				adapter.handleCheck(brickInstance, isChecked);
			}
		});

		TextView text = (TextView) view.findViewById(R.id.brick_set_bounce_factor_prototype_text_view);
		TextView edit = (TextView) view.findViewById(R.id.brick_set_bounce_factor_edit_text);

		getFormulaWithBrickField(BrickField.PHYSICS_BOUNCE_FACTOR).setTextFieldId(R.id.brick_set_bounce_factor_edit_text);
		getFormulaWithBrickField(BrickField.PHYSICS_BOUNCE_FACTOR).refreshTextField(view);

		text.setVisibility(View.GONE);
		edit.setVisibility(View.VISIBLE);
		edit.setOnClickListener(this);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_physics_set_bounce_factor, null);
		TextView textBounceFactor = (TextView) prototypeView
				.findViewById(R.id.brick_set_bounce_factor_prototype_text_view);
		textBounceFactor.setText(String.valueOf(BrickValues.PHYSIC_BOUNCE_FACTOR * 100));
		return prototypeView;
	}

	@Override
	public View getViewWithAlpha(int alphaValue) {

		if (view != null) {

			View layout = view.findViewById(R.id.brick_set_bounce_factor_layout);
			Drawable background = layout.getBackground();
			background.setAlpha(alphaValue);

			TextView textX = (TextView) view.findViewById(R.id.brick_set_bounce_factor_prototype_text_view);
			TextView editX = (TextView) view.findViewById(R.id.brick_set_bounce_factor_edit_text);
			textX.setTextColor(textX.getTextColors().withAlpha(alphaValue));
			editX.setTextColor(editX.getTextColors().withAlpha(alphaValue));
			editX.getBackground().setAlpha(alphaValue);

			this.alphaValue = alphaValue;
		}

		return view;
	}

	@Override
	public void showFormulaEditorToEditFormula(View view) {
		if (checkbox.getVisibility() == View.VISIBLE) {
			return;
		}
		FormulaEditorFragment.showFragment(view, this, BrickField.PHYSICS_BOUNCE_FACTOR);
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		sequence.addAction(sprite.getActionFactory().createSetBounceFactorAction(sprite,
				getFormulaWithBrickField(BrickField.PHYSICS_BOUNCE_FACTOR)));
		return null;
	}

	@Override
	public void updateReferenceAfterMerge(Project into, Project from) {
	}
}
