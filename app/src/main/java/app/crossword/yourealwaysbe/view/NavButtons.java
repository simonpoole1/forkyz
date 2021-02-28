package app.crossword.yourealwaysbe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import java.util.Set;

import app.crossword.yourealwaysbe.forkyz.R;
import app.crossword.yourealwaysbe.util.WeakSet;

public class NavButtons extends LinearLayout implements View.OnClickListener {
    private final Context context;
    private Set<NavButtonListener> listeners = WeakSet.buildSet();
    private static final enum Direction {
            LEFT, RIGHT, UP, DOWN
    };

    public static interface NavButtonListener {
        default void onNavButtonClick(Direction direction) { }
    }

    public NavButtons(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.nav_buttons, this);

        findViewById(R.id.nav_button_left).setOnClickListener(this);
        findViewById(R.id.nav_button_right).setOnClickListener(this);
        findViewById(R.id.nav_button_up).setOnClickListener(this);
        findViewById(R.id.nav_button_down).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        case R.id.nav_button_left:
            notifyNavButtonClick(Direction.LEFT);
            break;
        case R.id.nav_button_right:
            notifyNavButtonClick(Direction.RIGHT);
            break;
        case R.id.nav_button_up:
            notifyNavButtonClick(Direction.UP);
            break;
        case R.id.nav_button_down:
            notifyNavButtonClick(Direction.DOWN);
            break;
        }
    }

    public void addListener(NavButtonListener listener) {
        listeners.add(listener);
    }

    public void removeListener(NavButtonListener listener) {
        listeners.remove(listener);
    }

    private void notifyNavButtonClick(Direction direction) {
        for (NavButtonListener listener : listeners) {
            listener.onNavButtonClick(direction);
        }
    }

}
