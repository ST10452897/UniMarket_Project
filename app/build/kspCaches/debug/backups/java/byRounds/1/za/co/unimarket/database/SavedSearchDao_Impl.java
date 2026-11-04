package za.co.unimarket.database;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Double;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SavedSearchDao_Impl implements SavedSearchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SavedSearchEntity> __insertionAdapterOfSavedSearchEntity;

  public SavedSearchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSavedSearchEntity = new EntityInsertionAdapter<SavedSearchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `saved_searches` (`id`,`keyword`,`minPrice`,`maxPrice`,`category`,`campus`,`createdAt`) VALUES (?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SavedSearchEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getKeyword());
        if (entity.getMinPrice() == null) {
          statement.bindNull(3);
        } else {
          statement.bindDouble(3, entity.getMinPrice());
        }
        if (entity.getMaxPrice() == null) {
          statement.bindNull(4);
        } else {
          statement.bindDouble(4, entity.getMaxPrice());
        }
        if (entity.getCategory() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getCategory());
        }
        if (entity.getCampus() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCampus());
        }
        statement.bindLong(7, entity.getCreatedAt());
      }
    };
  }

  @Override
  public Object insertSavedSearch(final SavedSearchEntity search,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSavedSearchEntity.insert(search);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SavedSearchEntity>> getAllSavedSearchesFlow() {
    final String _sql = "SELECT * FROM saved_searches ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"saved_searches"}, new Callable<List<SavedSearchEntity>>() {
      @Override
      @NonNull
      public List<SavedSearchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKeyword = CursorUtil.getColumnIndexOrThrow(_cursor, "keyword");
          final int _cursorIndexOfMinPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "minPrice");
          final int _cursorIndexOfMaxPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "maxPrice");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfCampus = CursorUtil.getColumnIndexOrThrow(_cursor, "campus");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final List<SavedSearchEntity> _result = new ArrayList<SavedSearchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SavedSearchEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpKeyword;
            _tmpKeyword = _cursor.getString(_cursorIndexOfKeyword);
            final Double _tmpMinPrice;
            if (_cursor.isNull(_cursorIndexOfMinPrice)) {
              _tmpMinPrice = null;
            } else {
              _tmpMinPrice = _cursor.getDouble(_cursorIndexOfMinPrice);
            }
            final Double _tmpMaxPrice;
            if (_cursor.isNull(_cursorIndexOfMaxPrice)) {
              _tmpMaxPrice = null;
            } else {
              _tmpMaxPrice = _cursor.getDouble(_cursorIndexOfMaxPrice);
            }
            final String _tmpCategory;
            if (_cursor.isNull(_cursorIndexOfCategory)) {
              _tmpCategory = null;
            } else {
              _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            }
            final String _tmpCampus;
            if (_cursor.isNull(_cursorIndexOfCampus)) {
              _tmpCampus = null;
            } else {
              _tmpCampus = _cursor.getString(_cursorIndexOfCampus);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            _item = new SavedSearchEntity(_tmpId,_tmpKeyword,_tmpMinPrice,_tmpMaxPrice,_tmpCategory,_tmpCampus,_tmpCreatedAt);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
