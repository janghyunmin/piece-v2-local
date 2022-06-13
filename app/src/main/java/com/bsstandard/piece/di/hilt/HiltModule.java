package com.bsstandard.piece.di.hilt;
import javax.inject.Singleton;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

/**
 * packageName    : com.bsstandard.piece.module
 * fileName       : AppComponent
 * author         : piecejhm
 * date           : 2022/04/29
 * description    : DI - Hilt Component ( 추후 로직 개선시 사용 )
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022/04/29        piecejhm       최초 생성
 */

@Module
@Component(modules = {
//        RepositoryModule.class,
//        RetrofitModule.class,
//        UseCaseModule.class,
//        DatabaseModule.class
})

@Singleton
@InstallIn(SingletonComponent.class)
public interface HiltModule {

    @Component.Builder
    interface Builder {
        @BindsInstance
        //  Builder application(App application);

        HiltModule build();
    }

    //  void inject(App app);
}