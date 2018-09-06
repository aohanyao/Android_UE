package example.jc.com.blurmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.jaeger.library.StatusBarUtil;

import example.jc.com.blurmenu.helper.MoreMenuHelper;

public class MainActivity extends AppCompatActivity {
    private MoreMenuHelper mMoreMenuHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtil.setTranslucentForImageView(this, null);

        mMoreMenuHelper = new MoreMenuHelper(this, this.<RelativeLayout>findViewById(R.id.rl_more_menu_root));


        findViewById(R.id.fab_all_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoreMenuHelper.showMoreWindow();
            }
        });


        findViewById(R.id.fab_close_more_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMoreMenuHelper.closeMoreMenu(null);

            }
        });
    }
}
