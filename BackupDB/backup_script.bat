@echo off

REM MySQL database information
set DB_USER=root
set DB_PASSWORD=root
set DB_NAME=pialedadb

REM Backup directory
set BACKUP_DIR=F:\Dev\Client1\BackupDB

REM Timestamp (for backup filename)
set TIMESTAMP=%date:~10,4%%date:~4,2%%date:~7,2%%time:~0,2%%time:~3,2%%time:~6,2%

REM Create the backup file
set BACKUP_FILE=%BACKUP_DIR%\%DB_NAME%-%TIMESTAMP%.sql
mysqldump -u%DB_USER% -p%DB_PASSWORD% %DB_NAME% > "%BACKUP_FILE%"

