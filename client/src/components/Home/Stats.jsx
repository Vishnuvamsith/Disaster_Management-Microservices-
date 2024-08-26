import React, { useState, useEffect } from 'react';

export default function Stats() {
  const [disastersCount, setDisastersCount] = useState(0);
  const [lostLivesCount, setLostLivesCount] = useState(0);
  const [affectedCount, setAffectedCount] = useState(0);
  const [globalTotalCount, setGlobalTotalCount] = useState(0);

  useEffect(() => {
    const animateCounts = () => {
      const targetDisastersCount = 321;
      const targetLostLivesCount = 79732;
      const targetAffectedCount = 108;
      const targetGlobalTotalCount = 70;

      const step = 1; // You can adjust the step size for animation speed

      const animate = (current, target, setter) => {
        if (current < target) {
          setter(current);
          setTimeout(() => animate(current + step, target, setter), 10);
        } else {
          setter(target);
        }
      };

      animate(200, targetDisastersCount, setDisastersCount);
      animate(79600, targetLostLivesCount, setLostLivesCount);
      animate(0, targetAffectedCount, setAffectedCount);
      animate(0, targetGlobalTotalCount, setGlobalTotalCount);
    };

    animateCounts();
  }, []);

  return (
    <section className="stats_box py-10 grid place-items-center lg:grid-cols-4 grid-cols-2 gap-4 sm:w-9/12 w-11/12 mx-auto mt-6 px-10">
      <div>
        <h1 className="md:text-[40px] text-[25px] font-bold">{disastersCount}+</h1>
        <p>Disasters</p>
      </div>
      <div>
        <h1 className="md:text-[40px] text-[25px] font-bold">{lostLivesCount}</h1>
        <p>Lost Lives</p>
      </div>
      <div>
        <h1 className="md:text-[40px] text-[25px] font-bold">{affectedCount}Cr+</h1>
        <p>Affected</p>
      </div>
      <div>
        <h1 className="md:text-[40px] text-[25px] font-bold">{globalTotalCount}%</h1>
        <p>Of Global Total</p>
      </div>
    </section>
  );
}
