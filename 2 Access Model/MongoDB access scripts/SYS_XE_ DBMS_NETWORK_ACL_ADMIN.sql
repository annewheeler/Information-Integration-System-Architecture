BEGIN
  DBMS_NETWORK_ACL_ADMIN.append_host_ace (
      host       => 'localhost',
      lower_port => 8080,
      upper_port => 8080,
      ace        => xs$ace_type(
                      privilege_list => xs$name_list('http'),
                      principal_name => 'FDBO',
                      principal_type => xs_acl.ptype_db
                   )
  );
END;
/
COMMIT;

SELECT host,
       lower_port,
       upper_port,
       principal,
       privilege
FROM dba_host_aces
WHERE principal = 'FDBO';