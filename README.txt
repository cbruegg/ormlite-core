This is a fork of the ormlite-core library. The following functionality is changed / added:
- SQLExceptions are wrapped in more places for RuntimeExceptionDaos
- Raw queries are made easier and safer using RawQueryHelper.whereFromRaw
- The usage of multiple primary keys is made easier using the MultiplePrimaryKeyUtil
  which takes multiple column values and merges them into one.
- The CREATE helpers in TableUtils filter out duplicate column definitions
  if they are not id fields and at most one of them is not read-only.

This package provides the core functionality for the JDBC and Android packages.  Users
that are connecting to SQL databases via JDBC will need to download the ormlite-jdbc
package as well. Android users should download the ormlite-android package as well as
this core package.

For more information, see the online documentation on the home page:

   http://ormlite.com/

Sources can be found online via Github:

   https://github.com/j256/ormlite-core

Enjoy,
Gray Watson
