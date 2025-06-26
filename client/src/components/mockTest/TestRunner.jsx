import React, { useState } from 'react';
import axios from 'axios';

const TestRunner = ({ testId, questions, onComplete }) => {
  const [answers, setAnswers] = useState({});

  const handleChange = (qid, val) => {
    setAnswers({ ...answers, [qid]: val });
  };

  const handleSubmit = async () => {
    const payload = {
      mockTestId: testId,
      answers: Object.entries(answers).map(([questionId, selectedAnswer]) => ({
        questionId: parseInt(questionId),
        selectedAnswer
      }))
    };

    const res = await axios.post('/api/v1/mock-tests/submit', payload);
    onComplete(res.data);
  };

  return (
    <div className="space-y-6">
      {questions.map((q, idx) => (
        <div key={q.id} className="p-4 border rounded">
          <h3 className="font-semibold">{idx + 1}. {q.questionText}</h3>
          <div className="mt-2 space-y-2">
            {q.options?.map((opt, i) => (
              <label key={i} className="block">
                <input
                  type="radio"
                  name={`q_${q.id}`}
                  value={opt}
                  checked={answers[q.id] === opt}
                  onChange={() => handleChange(q.id, opt)}
                  className="mr-2"
                />
                {opt}
              </label>
            ))}
          </div>
        </div>
      ))}
      <button onClick={handleSubmit} className="bg-green-600 text-white px-6 py-2 rounded">
        Submit Test
      </button>
    </div>
  );
};

export default TestRunner;
