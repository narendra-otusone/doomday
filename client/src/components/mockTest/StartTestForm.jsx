import React, { useState } from 'react';
import axios from 'axios';

const StartTestForm = ({ userId, onStart }) => {
  const [subject, setSubject] = useState('');
  const [questionIds, setQuestionIds] = useState('');

  const handleStart = async () => {
    const ids = questionIds.split(',').map(id => parseInt(id.trim(), 10));

    const res = await axios.post('/api/v1/mock-tests/start', {
      userId,
      testTitle: `Mock Test - ${subject}`,
      subject,
      className: "10th",
      questionIds: ids
    });

    onStart(res.data.testId, res.data.questions);
  };

  return (
    <div className="space-y-4 border p-4 rounded shadow">
      <input
        type="text"
        placeholder="Subject"
        className="border p-2 w-full"
        value={subject}
        onChange={e => setSubject(e.target.value)}
      />
      <input
        type="text"
        placeholder="Comma-separated Question IDs"
        className="border p-2 w-full"
        value={questionIds}
        onChange={e => setQuestionIds(e.target.value)}
      />
      <button onClick={handleStart} className="bg-blue-600 text-white px-4 py-2 rounded">
        Start Test
      </button>
    </div>
  );
};

export default StartTestForm;
