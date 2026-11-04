package za.co.unimarket.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
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
public final class ProductDao_Impl implements ProductDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<ProductEntity> __insertionAdapterOfProductEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateWishlistStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteProduct;

  public ProductDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfProductEntity = new EntityInsertionAdapter<ProductEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `products` (`id`,`title`,`price`,`category`,`location`,`sellerName`,`sellerInitials`,`isVerified`,`isWishlisted`,`iconRes`,`bgColorRes`,`isSynced`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final ProductEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindDouble(3, entity.getPrice());
        statement.bindString(4, entity.getCategory());
        statement.bindString(5, entity.getLocation());
        statement.bindString(6, entity.getSellerName());
        statement.bindString(7, entity.getSellerInitials());
        final int _tmp = entity.isVerified() ? 1 : 0;
        statement.bindLong(8, _tmp);
        final int _tmp_1 = entity.isWishlisted() ? 1 : 0;
        statement.bindLong(9, _tmp_1);
        statement.bindLong(10, entity.getIconRes());
        statement.bindLong(11, entity.getBgColorRes());
        final int _tmp_2 = entity.isSynced() ? 1 : 0;
        statement.bindLong(12, _tmp_2);
      }
    };
    this.__preparedStmtOfUpdateWishlistStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE products SET isWishlisted = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteProduct = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM products WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertProducts(final List<ProductEntity> products,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductEntity.insert(products);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertProduct(final ProductEntity product,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfProductEntity.insert(product);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateWishlistStatus(final String productId, final boolean isWishlisted,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateWishlistStatus.acquire();
        int _argIndex = 1;
        final int _tmp = isWishlisted ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 2;
        _stmt.bindString(_argIndex, productId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateWishlistStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteProduct(final String productId,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteProduct.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, productId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteProduct.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductEntity>> getAllProductsFlow() {
    final String _sql = "SELECT * FROM products ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerInitials = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerInitials");
          final int _cursorIndexOfIsVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "isVerified");
          final int _cursorIndexOfIsWishlisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWishlisted");
          final int _cursorIndexOfIconRes = CursorUtil.getColumnIndexOrThrow(_cursor, "iconRes");
          final int _cursorIndexOfBgColorRes = CursorUtil.getColumnIndexOrThrow(_cursor, "bgColorRes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSellerName;
            _tmpSellerName = _cursor.getString(_cursorIndexOfSellerName);
            final String _tmpSellerInitials;
            _tmpSellerInitials = _cursor.getString(_cursorIndexOfSellerInitials);
            final boolean _tmpIsVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVerified);
            _tmpIsVerified = _tmp != 0;
            final boolean _tmpIsWishlisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWishlisted);
            _tmpIsWishlisted = _tmp_1 != 0;
            final int _tmpIconRes;
            _tmpIconRes = _cursor.getInt(_cursorIndexOfIconRes);
            final int _tmpBgColorRes;
            _tmpBgColorRes = _cursor.getInt(_cursorIndexOfBgColorRes);
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            _item = new ProductEntity(_tmpId,_tmpTitle,_tmpPrice,_tmpCategory,_tmpLocation,_tmpSellerName,_tmpSellerInitials,_tmpIsVerified,_tmpIsWishlisted,_tmpIconRes,_tmpBgColorRes,_tmpIsSynced);
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

  @Override
  public Object getAllProducts(final Continuation<? super List<ProductEntity>> $completion) {
    final String _sql = "SELECT * FROM products ORDER BY id DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerInitials = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerInitials");
          final int _cursorIndexOfIsVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "isVerified");
          final int _cursorIndexOfIsWishlisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWishlisted");
          final int _cursorIndexOfIconRes = CursorUtil.getColumnIndexOrThrow(_cursor, "iconRes");
          final int _cursorIndexOfBgColorRes = CursorUtil.getColumnIndexOrThrow(_cursor, "bgColorRes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSellerName;
            _tmpSellerName = _cursor.getString(_cursorIndexOfSellerName);
            final String _tmpSellerInitials;
            _tmpSellerInitials = _cursor.getString(_cursorIndexOfSellerInitials);
            final boolean _tmpIsVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVerified);
            _tmpIsVerified = _tmp != 0;
            final boolean _tmpIsWishlisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWishlisted);
            _tmpIsWishlisted = _tmp_1 != 0;
            final int _tmpIconRes;
            _tmpIconRes = _cursor.getInt(_cursorIndexOfIconRes);
            final int _tmpBgColorRes;
            _tmpBgColorRes = _cursor.getInt(_cursorIndexOfBgColorRes);
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            _item = new ProductEntity(_tmpId,_tmpTitle,_tmpPrice,_tmpCategory,_tmpLocation,_tmpSellerName,_tmpSellerInitials,_tmpIsVerified,_tmpIsWishlisted,_tmpIconRes,_tmpBgColorRes,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductEntity>> getWishlistedProductsFlow() {
    final String _sql = "SELECT * FROM products WHERE isWishlisted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerInitials = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerInitials");
          final int _cursorIndexOfIsVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "isVerified");
          final int _cursorIndexOfIsWishlisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWishlisted");
          final int _cursorIndexOfIconRes = CursorUtil.getColumnIndexOrThrow(_cursor, "iconRes");
          final int _cursorIndexOfBgColorRes = CursorUtil.getColumnIndexOrThrow(_cursor, "bgColorRes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSellerName;
            _tmpSellerName = _cursor.getString(_cursorIndexOfSellerName);
            final String _tmpSellerInitials;
            _tmpSellerInitials = _cursor.getString(_cursorIndexOfSellerInitials);
            final boolean _tmpIsVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVerified);
            _tmpIsVerified = _tmp != 0;
            final boolean _tmpIsWishlisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWishlisted);
            _tmpIsWishlisted = _tmp_1 != 0;
            final int _tmpIconRes;
            _tmpIconRes = _cursor.getInt(_cursorIndexOfIconRes);
            final int _tmpBgColorRes;
            _tmpBgColorRes = _cursor.getInt(_cursorIndexOfBgColorRes);
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            _item = new ProductEntity(_tmpId,_tmpTitle,_tmpPrice,_tmpCategory,_tmpLocation,_tmpSellerName,_tmpSellerInitials,_tmpIsVerified,_tmpIsWishlisted,_tmpIconRes,_tmpBgColorRes,_tmpIsSynced);
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

  @Override
  public Object getWishlistedProducts(final Continuation<? super List<ProductEntity>> $completion) {
    final String _sql = "SELECT * FROM products WHERE isWishlisted = 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerInitials = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerInitials");
          final int _cursorIndexOfIsVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "isVerified");
          final int _cursorIndexOfIsWishlisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWishlisted");
          final int _cursorIndexOfIconRes = CursorUtil.getColumnIndexOrThrow(_cursor, "iconRes");
          final int _cursorIndexOfBgColorRes = CursorUtil.getColumnIndexOrThrow(_cursor, "bgColorRes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSellerName;
            _tmpSellerName = _cursor.getString(_cursorIndexOfSellerName);
            final String _tmpSellerInitials;
            _tmpSellerInitials = _cursor.getString(_cursorIndexOfSellerInitials);
            final boolean _tmpIsVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVerified);
            _tmpIsVerified = _tmp != 0;
            final boolean _tmpIsWishlisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWishlisted);
            _tmpIsWishlisted = _tmp_1 != 0;
            final int _tmpIconRes;
            _tmpIconRes = _cursor.getInt(_cursorIndexOfIconRes);
            final int _tmpBgColorRes;
            _tmpBgColorRes = _cursor.getInt(_cursorIndexOfBgColorRes);
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            _item = new ProductEntity(_tmpId,_tmpTitle,_tmpPrice,_tmpCategory,_tmpLocation,_tmpSellerName,_tmpSellerInitials,_tmpIsVerified,_tmpIsWishlisted,_tmpIconRes,_tmpBgColorRes,_tmpIsSynced);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<ProductEntity>> searchProductsFlow(final String query) {
    final String _sql = "SELECT * FROM products WHERE title LIKE '%' || ? || '%' OR category LIKE '%' || ? || '%'";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    _statement.bindString(_argIndex, query);
    _argIndex = 2;
    _statement.bindString(_argIndex, query);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"products"}, new Callable<List<ProductEntity>>() {
      @Override
      @NonNull
      public List<ProductEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfPrice = CursorUtil.getColumnIndexOrThrow(_cursor, "price");
          final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "category");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSellerName = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerName");
          final int _cursorIndexOfSellerInitials = CursorUtil.getColumnIndexOrThrow(_cursor, "sellerInitials");
          final int _cursorIndexOfIsVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "isVerified");
          final int _cursorIndexOfIsWishlisted = CursorUtil.getColumnIndexOrThrow(_cursor, "isWishlisted");
          final int _cursorIndexOfIconRes = CursorUtil.getColumnIndexOrThrow(_cursor, "iconRes");
          final int _cursorIndexOfBgColorRes = CursorUtil.getColumnIndexOrThrow(_cursor, "bgColorRes");
          final int _cursorIndexOfIsSynced = CursorUtil.getColumnIndexOrThrow(_cursor, "isSynced");
          final List<ProductEntity> _result = new ArrayList<ProductEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final ProductEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final double _tmpPrice;
            _tmpPrice = _cursor.getDouble(_cursorIndexOfPrice);
            final String _tmpCategory;
            _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSellerName;
            _tmpSellerName = _cursor.getString(_cursorIndexOfSellerName);
            final String _tmpSellerInitials;
            _tmpSellerInitials = _cursor.getString(_cursorIndexOfSellerInitials);
            final boolean _tmpIsVerified;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsVerified);
            _tmpIsVerified = _tmp != 0;
            final boolean _tmpIsWishlisted;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsWishlisted);
            _tmpIsWishlisted = _tmp_1 != 0;
            final int _tmpIconRes;
            _tmpIconRes = _cursor.getInt(_cursorIndexOfIconRes);
            final int _tmpBgColorRes;
            _tmpBgColorRes = _cursor.getInt(_cursorIndexOfBgColorRes);
            final boolean _tmpIsSynced;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfIsSynced);
            _tmpIsSynced = _tmp_2 != 0;
            _item = new ProductEntity(_tmpId,_tmpTitle,_tmpPrice,_tmpCategory,_tmpLocation,_tmpSellerName,_tmpSellerInitials,_tmpIsVerified,_tmpIsWishlisted,_tmpIconRes,_tmpBgColorRes,_tmpIsSynced);
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
