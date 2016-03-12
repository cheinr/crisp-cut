package com.chein.crispcut.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.chein.crispcut.LogCutter;


public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(320, 480);
        }

        @Override
        public ApplicationListener getApplicationListener () {
                return new LogCutter();
        }
}