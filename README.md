The application processes a file from the path provided in argument.
The process goes through the following steps:
1) validation
2) evaluation
3) reporting

The validation constists of
- structural validation
  1) The header line must consist of the following fields case-sensitively: Id,firstName,lastName,salary,managerId
  2) every value of an employee is mandatory except managerId
- logical validation
  1) There must be exactly one CEO
  2) Every employee assigned as manager for any other employee must exist in the file
  3) circular reporting lines are not allowed.
  4) Assigning to an employee themself as manager is not allowed
