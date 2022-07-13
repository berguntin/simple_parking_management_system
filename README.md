# simple_parking_management_system

This is my final proyect for Java Foundations certificate course by Oracle Academy.

It uses a MySQL database for storing data and JDBC as connection manager. You can found database creation script in main folder.

GUI is made in Java Swing.

Allows users to record entry of a vehicle with spanish registration plate´s format (before 2000´s: XX-0000-XX || after 2000´s: 0000-XXX)
any other format is not allowed. It´s verified with a regular expression for each format.

When user´s want to take his vehicle out of the system, it calculates the amount to pay and make a takeout registration at database.

