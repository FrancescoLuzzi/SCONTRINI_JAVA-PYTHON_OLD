import json
import sys
import mariadb
import os
from dotenv import load_dotenv


class Saver:
    r"""
    this class is designed to store a json copy of a txt file
    of receipts and to maintain the main file ,
    given the <paths>
    """

    def __init__(self, main_path, main_file, json_file):
        self.main_path = main_path
        self.main_file = main_file
        self.json_file = json_file
        self.main_lines = []
        self.conn = None
        self.curr = None

    def init_connection(self):
        load_dotenv(dotenv_path="/path/.env")
        try:
            self.conn = mariadb.connect(
                user=os.getenv("USER"),
                password=os.getenv("PASSWD"),
                host=os.getenv("DB_IP"),
                port=os.getenv("DB_PORT"),
                database=os.getenv("DB_NAME"),
            )
            self.curr = self.conn.cursor()
        except mariadb.Error:
            print("errore nella connessione")
            sys.exit(1)
        """
        starts connection to DB
        """

    def save_to_db(self):
        if self.conn == None:
            self.init_connection()
        query = "INSERT INTO scontr(tipo, data, prezzo) VALUES (?, ?, ?)"
        with open(self.main_path.__add__(self.json_file), "r") as infile:
            diz = json.load(infile)
            prezzo = 0
            data = ""
            for key in diz:
                for cosi in diz[key]:
                    cosi = cosi.split(" ")
                    prezzo = float(cosi[1].replace(",", "."))
                    cosi = cosi[2].split("/")
                    data = f"20{cosi[2]}-{cosi[1]}-{cosi[0]}"
                    self.curr.execute(query, (key, data, prezzo))
        self.conn.commit()
        input("Scritto tutto sul db, premi un tasto per chiudere")

    def jsoner(self, dizionario):
        with open(self.main_path.__add__(self.json_file), "w") as outfile:
            json.dump(dizionario, outfile, indent=1, sort_keys=True)
        """
        given a dictionary it will be dumped all in the .json_file
        """

    def finalize(self):
        self.conn.close()
        """
        closes DB connection
        """

    def save_from_json_main(self):
        with open(self.main_path.__add__(self.json_file), "r") as infile:
            diz = json.load(infile)
            with open(self.main_path.__add__(self.main_file), "w") as outfile:
                for key in diz:
                    for cosi in diz[key]:
                        cosi = cosi.split(" ")
                        outstring = key + "| € " + cosi[1] + " |" + cosi[2] + "\n"
                        outfile.write(outstring)

    def save_to_other_file_from_json(self, name):
        with open(self.main_path.__add__(self.json_file), "r") as infile:
            diz = json.load(infile)
            with open(self.main_path.__add__(name), "w") as outfile:
                for key in diz:
                    for cosi in diz[key]:
                        cosi = cosi.split(" ")
                        outstring = key + "| € " + cosi[1] + " |" + cosi[2] + "\n"
                        outfile.write(outstring)

    def run_option(self, opzione):
        if opzione == 1:
            self.save_from_json_main()
        elif opzione == 2:
            name = str(input("scrivi il nome del file\n"))
            count = 0
            while name == "" and count < 3:
                name = str(
                    input("qualcosa è andato storto!!! Scrivi il nome del file \n")
                )
                count += 1
            if count < 3:
                if not name.endswith(".txt"):
                    name += ".txt"
                self.save_to_other_file_from_json(name)
            else:
                print("Triplo strike... è andata male")
        elif opzione == 3:
            self.save_to_db()

    def reader(self):
        with open(self.main_path.__add__(self.main_file), "r") as main:
            self.main_lines = self.file_reader(main)

    def num_car_main(self):
        resto = self.main_lines.__len__()
        caratteri = 1
        while True:
            resto = resto / 10
            if resto >= 1:
                caratteri += 1
            else:
                break
        return caratteri

    def __repr__(self):
        output = "Numero di scontrini\n\t"
        output += str(self.main_lines.__len__())
        return output

    def file_reader(self, file):
        lines = []
        k = " "
        while True:
            k = file.readline()
            if k == "":
                break
            lines.append(k)
        return lines

    def notZero(self):
        if self.main_lines.__len__() == 0:
            return False
        return True


def dictioner(lista):
    tipo = lista[0].split("|")[0].strip()
    diz = {}
    temp = []
    for line in lista:
        parts = [l.strip() for l in line.split("|")]
        parts[1] = parts[1].replace("€", "euro")
        if tipo != parts[0]:
            diz[tipo] = temp
            temp = []
            tipo = parts[0]
        temp.append(" ".join(parts[1:]))
    diz[tipo] = temp
    return diz

