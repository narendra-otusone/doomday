import React from 'react';

const TestResult = ({ result }) => {
  return (
    <div className="bg-gray-100 p-4 rounded mt-6 shadow">
      <h2 className="text-xl font-bold mb-2">Test Result</h2>
      <p>Total Marks: {result.totalMarks}</p>
      <p>Marks Obtained: {result.marksObtained}</p>
      <p>Correct Answers: {result.correctAnswers}</p>
      <p>Incorrect Answers: {result.incorrectAnswers}</p>
      <p className="font-semibold">Performance Level: {result.performanceLevel}</p>
    </div>
  );
};

export default TestResult;
