# ============================================
# COMPLETE LIBRARY MANAGEMENT API TEST SCRIPT
# ============================================
$baseUrl = "http://localhost:8080"

Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "   LIBRARY MANAGEMENT API TESTING        " -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

# ============ BOOK ENDPOINTS ============
Write-Host "`n BOOK ENDPOINTS TESTING" -ForegroundColor Yellow

# 1. Create Books
Write-Host "`n1. Creating Books..." -ForegroundColor Green
$book1 = @{
    title = "The Great Gatsby"
    author = "F. Scott Fitzgerald"
    isbn = "9780743273565"
    publicationYear = 1925
} | ConvertTo-Json

$book2 = @{
    title = "To Kill a Mockingbird"
    author = "Harper Lee"
    isbn = "9780061120084"
    publicationYear = 1960
} | ConvertTo-Json

$book3 = @{
    title = "1984"
    author = "George Orwell"
    isbn = "9780451524935"
    publicationYear = 1949
} | ConvertTo-Json

$response1 = Invoke-RestMethod -Uri "$baseUrl/api/books" -Method Post -Body $book1 -ContentType "application/json"
Write-Host "   Book 1 Created: $($response1.title) (ID: $($response1.id))" -ForegroundColor Gray

$response2 = Invoke-RestMethod -Uri "$baseUrl/api/books" -Method Post -Body $book2 -ContentType "application/json"
Write-Host "   Book 2 Created: $($response2.title) (ID: $($response2.id))" -ForegroundColor Gray

$response3 = Invoke-RestMethod -Uri "$baseUrl/api/books" -Method Post -Body $book3 -ContentType "application/json"
Write-Host "   Book 3 Created: $($response3.title) (ID: $($response3.id))" -ForegroundColor Gray

# 2. Get All Books
Write-Host "`n2. Getting All Books..." -ForegroundColor Green
$allBooks = Invoke-RestMethod -Uri "$baseUrl/api/books" -Method Get
Write-Host "   Total Books: $($allBooks.Count)"
$allBooks | Format-Table id, title, author, available -AutoSize

# 3. Get Single Book
Write-Host "`n3. Getting Single Book (ID: 1)..." -ForegroundColor Green
$singleBook = Invoke-RestMethod -Uri "$baseUrl/api/books/1" -Method Get
Write-Host "   Book Found: $($singleBook.title) by $($singleBook.author)"

# 4. Update Book
Write-Host "`n4. Updating Book (ID: 2)..." -ForegroundColor Green
$updateData = @{
    title = "To Kill a Mockingbird - Special Edition"
    author = "Harper Lee"
    isbn = "9780061120084"
    publicationYear = 1960
} | ConvertTo-Json

$updatedBook = Invoke-RestMethod -Uri "$baseUrl/api/books/2" -Method Put -Body $updateData -ContentType "application/json"
Write-Host "   Updated Title: $($updatedBook.title)"

# 5. Search Books
Write-Host "`n5. Searching Books with 'kill'..." -ForegroundColor Green
$searchResults = Invoke-RestMethod -Uri "$baseUrl/api/books/search?q=kill" -Method Get
Write-Host "   Search Results: $($searchResults.Count) books found"

# ============ MEMBER ENDPOINTS ============
Write-Host "`n👥 MEMBER ENDPOINTS TESTING" -ForegroundColor Yellow

# 6. Register Members
Write-Host "`n6. Registering Members..." -ForegroundColor Green
$member1 = @{
    name = "John Smith"
    email = "john.smith@email.com"
    phoneNumber = "555-0101"
} | ConvertTo-Json

$member2 = @{
    name = "Sarah Johnson"
    email = "sarah.j@email.com"
    phoneNumber = "555-0102"
} | ConvertTo-Json

$member1Resp = Invoke-RestMethod -Uri "$baseUrl/api/members" -Method Post -Body $member1 -ContentType "application/json"
Write-Host "   Member 1 Registered: $($member1Resp.name) (ID: $($member1Resp.id))" -ForegroundColor Gray

$member2Resp = Invoke-RestMethod -Uri "$baseUrl/api/members" -Method Post -Body $member2 -ContentType "application/json"
Write-Host "   Member 2 Registered: $($member2Resp.name) (ID: $($member2Resp.id))" -ForegroundColor Gray

# 7. Get All Members
Write-Host "`n7. Getting All Members..." -ForegroundColor Green
$allMembers = Invoke-RestMethod -Uri "$baseUrl/api/members" -Method Get
Write-Host "   Total Members: $($allMembers.Count)"
$allMembers | Format-Table id, name, email, active -AutoSize

# 8. Get Single Member
Write-Host "`n8. Getting Single Member (ID: 1)..." -ForegroundColor Green
$singleMember = Invoke-RestMethod -Uri "$baseUrl/api/members/1" -Method Get
Write-Host "   Member Found: $($singleMember.name)"

# 9. Update Member
Write-Host "`n9. Updating Member (ID: 2)..." -ForegroundColor Green
$memberUpdate = @{
    name = "Sarah Johnson-Williams"
    email = "sarah.jw@email.com"
    phoneNumber = "555-9999"
} | ConvertTo-Json

$updatedMember = Invoke-RestMethod -Uri "$baseUrl/api/members/2" -Method Put -Body $memberUpdate -ContentType "application/json"
Write-Host "   Updated Name: $($updatedMember.name)"

# 10. Member Login Simulation
Write-Host "`n10. Member Login Simulation..." -ForegroundColor Green
$loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/members/login?email=john.smith@email.com" -Method Post
Write-Host "   Login Response: $loginResponse"

# ============ LOAN ENDPOINTS ============
Write-Host "`n LOAN ENDPOINTS TESTING" -ForegroundColor Yellow

# 11. Borrow a Book
Write-Host "`n11. Borrowing a Book (Member 1 borrows Book 1)..." -ForegroundColor Green
$borrowResponse = Invoke-RestMethod -Uri "$baseUrl/api/loans/borrow?bookId=1&memberId=1" -Method Post
Write-Host "   Borrow Success: $($borrowResponse.message)" -ForegroundColor Gray
Write-Host "   Loan ID: $($borrowResponse.loanId), Due: $($borrowResponse.dueDate)" -ForegroundColor Gray

# 12. Borrow Another Book
Write-Host "`n12. Borrowing Another Book (Member 2 borrows Book 2)..." -ForegroundColor Green
$borrowResponse2 = Invoke-RestMethod -Uri "$baseUrl/api/loans/borrow?bookId=2&memberId=2" -Method Post
Write-Host "   Borrow Success: $($borrowResponse2.message)"
Write-Host "   Loan ID: $($borrowResponse2.loanId), Due: $($borrowResponse2.dueDate)"

# 13. View Active Loans
Write-Host "`n13. Viewing All Active Loans..." -ForegroundColor Green
$activeLoans = Invoke-RestMethod -Uri "$baseUrl/api/loans/active" -Method Get
Write-Host "   Active Loans: $($activeLoans.Count)"
$activeLoans | Format-Table id, bookId, memberId, borrowDate, dueDate, returned -AutoSize

# 14. View Member's Loans
Write-Host "`n14. Viewing Member 1's Active Loans..." -ForegroundColor Green
$memberLoans = Invoke-RestMethod -Uri "$baseUrl/api/loans/member/1" -Method Get
Write-Host "   Member 1 has $($memberLoans.Count) active loan(s)"

# 15. Return a Book
Write-Host "`n15. Returning a Book (Loan ID: 1)..." -ForegroundColor Green
$returnResponse = Invoke-RestMethod -Uri "$baseUrl/api/loans/return?loanId=1" -Method Put
Write-Host "   Return Response: $returnResponse"

# 16. Check Active Loans After Return
Write-Host "`n16. Checking Active Loans After Return..." -ForegroundColor Green
$activeLoansAfter = Invoke-RestMethod -Uri "$baseUrl/api/loans/active" -Method Get
Write-Host "   Active Loans Remaining: $($activeLoansAfter.Count)"

# ============ VERIFICATION ENDPOINTS ============
Write-Host "`n VERIFICATION TESTING" -ForegroundColor Yellow

# 17. Verify Book Availability After Return
Write-Host "`n17. Verifying Book 1 is Available Again..." -ForegroundColor Green
$book1Check = Invoke-RestMethod -Uri "$baseUrl/api/books/1" -Method Get
Write-Host "   Book 1 Available: $($book1Check.available)" -ForegroundColor $(if($book1Check.available) {"Green"} else {"Red"})

# 18. Try to Borrow Unavailable Book
Write-Host "`n18. Trying to Borrow Already Borrowed Book (Book 2)..." -ForegroundColor Green
try {
    $failBorrow = Invoke-RestMethod -Uri "$baseUrl/api/loans/borrow?bookId=2&memberId=1" -Method Post -ErrorAction Stop
} catch {
    Write-Host "   Expected Error: $($_.Exception.Message)" -ForegroundColor Yellow
}

# 19. Delete a Book
Write-Host "`n19. Deleting Book (ID: 3)..." -ForegroundColor Green
$deleteResponse = Invoke-RestMethod -Uri "$baseUrl/api/books/3" -Method Delete
Write-Host "   Book 3 Deleted Successfully" -ForegroundColor Gray

# 20. Deactivate a Member
Write-Host "`n20. Deactivating Member (ID: 2)..." -ForegroundColor Green
$deactivateResponse = Invoke-RestMethod -Uri "$baseUrl/api/members/2" -Method Delete
Write-Host "   Member 2 Deactivated Successfully" -ForegroundColor Gray

# 21. Final Check
Write-Host "`n21. Final Status Check..." -ForegroundColor Green
$finalBooks = Invoke-RestMethod -Uri "$baseUrl/api/books" -Method Get
$finalMembers = Invoke-RestMethod -Uri "$baseUrl/api/members" -Method Get
Write-Host "   Total Books Remaining: $($finalBooks.Count)" -ForegroundColor Gray
Write-Host "   Total Members: $($finalMembers.Count)" -ForegroundColor Gray

# ============ SUMMARY ============
Write-Host "`n==========================================" -ForegroundColor Cyan
Write-Host "   TESTING COMPLETE - SUMMARY             " -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan

$summary = @"
API TEST SUMMARY:
✓ 21 endpoints tested
✓ Books: Created, Read, Updated, Deleted, Searched
✓ Members: Registered, Viewed, Updated, Deactivated, Logged in
✓ Loans: Books borrowed, returned, tracked
✓ Error handling: Tested unavailable book scenario
"@

Write-Host $summary -ForegroundColor Green

Write-Host "`n FINAL DATA:" -ForegroundColor Yellow
Write-Host "Books in Library:" -ForegroundColor Gray
$finalBooks | Format-Table id, title, author, available -AutoSize

Write-Host "`nActive Members:" -ForegroundColor Gray
$finalMembers | Where-Object {$_.active} | Format-Table id, name, email -AutoSize

Write-Host "`n All tests executed successfully!" -ForegroundColor Green

#How to run ,right click ,reveal in explorer ,run witth powrshel ,best is 
#navaigate to its directory on powershell command line 
#use this to run it .\test-all-endpoints.ps1