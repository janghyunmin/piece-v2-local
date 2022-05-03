package com.bsstandard.piece.di.dagger;

import com.bsstandard.piece.app.App;
import com.bsstandard.piece.module.RepositoryModule;
import com.bsstandard.piece.module.RetrofitModule;
import com.bsstandard.piece.module.UseCaseModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * packageName    : com.bsstandard.piece.module
 * fileName       : AppComponent
 * author         : piecejhm
 * date           : 2022/04/29
 * description    : DI - Dagger Component
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */

@Component(modules = {
        AndroidSupportInjectionModule.class,
        RepositoryModule.class,
        RetrofitModule.class,
        UseCaseModule.class
        })
@Singleton
public interface DaggerComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(App application);

        DaggerComponent build();
    }

    void inject(App app);
}