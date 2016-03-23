package com.j256.ormlite.misc;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Base class that your data elements can extend which allow them to refresh, update, etc. themselves. ORMLite will
 * automagically set the appropriate {@link Dao} on the class if it is received by a query but if you are trying to
 * create the class, you will need to either create it through the DAO or set the dao on it directly with
 * {@link #setDao(Dao)}.
 * 
 * <p>
 * <b>NOTE:</b> The default pattern is to use the {@link Dao} classes to operate on your data classes. This will allow
 * your data classes to have their own hierarchy and isolates the database code in the Daos. However, you are free to
 * use this base class if you prefer this pattern.
 * </p>
 * 
 * <p>
 * <b>NOTE:</b> The internal Dao field has been marked with transient so that it won't be serialized (thanks jc). If you
 * do de-serialize on these classes, you will need to refresh it with the Dao to get it to work again.
 * </p>
 * 
 * @author graywatson
 */
public abstract class BaseDaoEnabled<T, ID> {

	protected transient Dao<T, ID> dao;

	/**
	 * A call through to the {@link Dao#create(Object)}.
	 */
	public int create() throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.create(t);
	}

	/**
	 * A call through to the {@link Dao#refresh(Object)}.
	 */
	public int refresh() throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.refresh(t);
	}

	/**
	 * A call through to the {@link Dao#update(Object)}.
	 */
	public int update() throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.update(t);
	}

	/**
	 * A call through to the {@link Dao#updateId(Object, Object)}.
	 */
	public int updateId(ID newId) throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.updateId(t, newId);
	}

	/**
	 * A call through to the {@link Dao#delete(Object)}.
	 */
	public int delete() throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.delete(t);
	}

	/**
	 * A call through to the {@link Dao#objectToString(Object)}.
	 */
	public String objectToString() {
		try {
			checkForDao();
		} catch (SQLException e) {
			throw new IllegalArgumentException(e);
		}
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.objectToString(t);
	}

	/**
	 * A call through to the {@link Dao#extractId(Object)}.
	 */
	public ID extractId() throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.extractId(t);
	}

	/**
	 * A call through to the {@link Dao#objectsEqual(Object, Object)}.
	 */
	public boolean objectsEqual(T other) throws SQLException {
		checkForDao();
		@SuppressWarnings("unchecked")
		T t = (T) this;
		return dao.objectsEqual(t, other);
	}

	/**
	 * Set the {@link Dao} on the object. For the {@link #create()} call to work, this must be done beforehand by the
	 * caller. If the object has been received from a query call to the Dao then this should have been set
	 * automagically.
	 */
	public void setDao(Dao<T, ID> dao) {
		this.dao = dao;
	}

	/**
	 * Return the DAO object associated with this object. It will be null unless the object was generated by a query
	 * call to the database or {@link #setDao(Dao)} has already been called.
	 */
	public Dao<T, ID> getDao() {
		return dao;
	}

	private void checkForDao() throws SQLException {
		if (dao == null) {
			throw new SQLException("Dao has not been set on " + getClass() + " object: " + this);
		}
	}

	/**
	 * @see MultiplePrimaryKeyUtil#mergeIntoOneColumn(String, String...)
	 */
	protected static String mergeIntoOneColumn(String separator, String... values) {
		return MultiplePrimaryKeyUtil.mergeIntoOneColumn(separator, values);
	}

	/**
	 * @see MultiplePrimaryKeyUtil#mergeIntoOneColumn(String...)
	 */
	protected static String mergeIntoOneColumn(String... values) {
		return MultiplePrimaryKeyUtil.mergeIntoOneColumn(values);
	}
}
