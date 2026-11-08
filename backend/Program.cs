using Google.Cloud.Firestore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var projectId = builder.Configuration["Firestore:ProjectId"]
    ?? throw new InvalidOperationException(
        "Firestore ProjectId is missing.");

builder.Services.AddSingleton(
    FirestoreDb.Create(projectId)
);

var app = builder.Build();

if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();
app.MapControllers();
app.Run();